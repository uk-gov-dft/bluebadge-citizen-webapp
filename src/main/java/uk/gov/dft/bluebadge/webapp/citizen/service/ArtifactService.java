package uk.gov.dft.bluebadge.webapp.citizen.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.dft.bluebadge.webapp.citizen.config.S3Config;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Service
@Slf4j
public class ArtifactService {
  public static final String ENCODING_CHAR_SET = "UTF-8";
  private static final Set<String> ACCEPTED_MIME_TYPES =
      ImmutableSet.of("image/jpeg", "image/gif", "image/png", "application/pdf");
  private final AmazonS3 amazonS3;
  private final S3Config s3Config;
  private final TransferManager transferManager;

  @Autowired
  public ArtifactService(TransferManager transferManager, AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
    this.transferManager = transferManager;
  }

  public JourneyArtifact upload(MultipartFile multipartFile)
      throws IOException, InterruptedException {
    Assert.notNull(multipartFile, "Multipart file is null.");

    if (multipartFile.isEmpty()) {
      throw new IllegalArgumentException("Upload failed. JourneyArtifact is empty");
    }

    log.info(
        "Uploading document to S3. {}, size:{}",
        multipartFile.getOriginalFilename(),
        multipartFile.getSize());

    String keyName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();

    keyName = URLEncoder.encode(keyName, ENCODING_CHAR_SET);
    ObjectMetadata meta = new ObjectMetadata();
    meta.setContentLength(multipartFile.getSize());
    String mimetype = determineMimeType(multipartFile.getOriginalFilename());
    meta.setContentType(mimetype);
    Upload upload =
        transferManager.upload(
            s3Config.getS3Bucket(), keyName, multipartFile.getInputStream(), meta);
    UploadResult uploadResult = upload.waitForUploadResult();
    URL url = amazonS3.getUrl(uploadResult.getBucketName(), uploadResult.getKey());
    URL signedS3Url = generateSignedS3Url(uploadResult.getKey());

    return JourneyArtifact.builder()
        .fileName(multipartFile.getOriginalFilename())
        .type(determineFileType(mimetype))
        .url(url)
        .signedUrl(signedS3Url)
        .build();
  }

  private static String determineMimeType(String filename) {
    String mimetype = Mimetypes.getInstance().getMimetype(filename);
    if (!ACCEPTED_MIME_TYPES.contains(mimetype)) {
      throw new UnsupportedMimetypeException(mimetype);
    }
    return mimetype;
  }

  private static String determineFileType(String mimeType) {
    if (null != mimeType && mimeType.startsWith("image/")) {
      return "image";
    }
    return "file";
  }

  private URL generateSignedS3Url(String link) {
    if (null == link) {
      return null;
    }

    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += s3Config.getSignedUrlDurationMs();
    expiration.setTime(expTimeMillis);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(s3Config.getS3Bucket(), link)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);
    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
  }

  public void createAccessibleLinks(JourneyArtifact journeyArtifact) {
    Assert.notNull(journeyArtifact, "The artifact is null.");
    URL url = journeyArtifact.getUrl();
    try {
      AmazonS3URI amazonS3URI = new AmazonS3URI(url.toURI());
      log.debug(
          "Create accessible link. Extracted bucket:{}, key:{}, from link:{}",
          amazonS3URI.getBucket(),
          amazonS3URI.getKey(),
          url);
      URL signedS3Url = generateSignedS3Url(amazonS3URI.getKey());
      journeyArtifact.setSignedUrl(signedS3Url);
    } catch (Exception e) {
      log.error("Failed to create accessible link from url:{}", url, e);
    }
  }
}