package uk.gov.dft.bluebadge.webapp.citizen.service;

import com.amazonaws.HttpMethod;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.dft.bluebadge.webapp.citizen.config.S3Config;
import uk.gov.dft.bluebadge.webapp.citizen.model.Document;

@Service
@Slf4j
public class DocumentService {
  private final AmazonS3 amazonS3;
  private final S3Config s3Config;
  private TransferManager transferManager;

  public DocumentService(AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
    transferManager =
        TransferManagerBuilder.standard()
            .withS3Client(amazonS3)
            .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
            .build();
  }

  public Document uploadDocument(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new IllegalArgumentException("Upload failed. Document is empty");
    }

    log.info(
        "Uploading document to S3. {}, size:{}",
        multipartFile.getOriginalFilename(),
        multipartFile.getSize());

    String keyName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();

    try {
      ObjectMetadata meta = new ObjectMetadata();
      meta.setContentLength(multipartFile.getSize());
      Upload upload =
          transferManager.upload(
              s3Config.getS3Bucket(), keyName, multipartFile.getInputStream(), meta);
      UploadResult uploadResult = upload.waitForUploadResult();
      URL url = amazonS3.getUrl(uploadResult.getBucketName(), uploadResult.getKey());
      URL signedS3Url = generateSignedS3Url(uploadResult.getKey());
      return Document.builder()
          .fileName(multipartFile.getOriginalFilename())
          .type("file")
          .url(url)
          .signedUrl(signedS3Url)
          .build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to upload document.", e);
    }
  }

  private URL generateSignedS3Url(String link) {
    if (null == link) {
      return null;
    }

    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += s3Config.getSignedUrlDurationMs();
    expiration.setTime(expTimeMillis);

    // Generate the pre-signed URL with expiry.
    //    try {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(s3Config.getS3Bucket(), link)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);
    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    //    }
    //    catch (AmazonServiceException e) {
    //      throw handleSdkClientException(
    //          e, "Generate signed url for image failed, s3 storage could not process request.");
    //    } catch (SdkClientException e) {
    //      throw handleSdkClientException(
    //          e, "Generate signed url for image failed, s3 storage could not be contacted.");
    //    }
  }
}
