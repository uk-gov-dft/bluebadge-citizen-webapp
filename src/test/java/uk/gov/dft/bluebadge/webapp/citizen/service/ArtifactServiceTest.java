package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_PDF_MIME_TYPES;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import java.net.URL;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.dft.bluebadge.webapp.citizen.config.S3Config;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

class ArtifactServiceTest {
  private static final int URL_DURATION_MS = 60000;
  private ArtifactService artifactService;

  @Mock AmazonS3 amazonS3Mock;
  @Mock TransferManager transferManagerMock;
  @Mock FileCheckerService fileCheckerServiceMock;
  private URL s3ObjectURL;
  private URL s3SignedUrl;
  private URL validS3ObjectUrl;

  @BeforeEach
  @SneakyThrows
  void setup() {
    initMocks(this);
    S3Config s3Config = new S3Config();
    s3Config.setS3Bucket("test_bucket");
    s3Config.setSignedUrlDurationMs(URL_DURATION_MS);
    artifactService =
        new ArtifactService(transferManagerMock, amazonS3Mock, s3Config, fileCheckerServiceMock);

    s3ObjectURL = new URL("http://test");
    s3SignedUrl = new URL("http://testSigned");
    validS3ObjectUrl =
        new URL(
            "https://dev-dft-badges.s3.eu-west-2.amazonaws.com/6b784cd1-9c30-4650-a907-b6fd3ccfb4da-original.jpg");
  }

  @Test
  void upload() {
    setupMocksForUpload("resultBucket", "resultKey");

    String testUpload = "Some thing to upload";
    MultipartFile multipartFile =
        new MockMultipartFile("testFile", "originalFile.jpg", "", testUpload.getBytes());
    JourneyArtifact journeyArtifact = artifactService.upload(multipartFile, IMAGE_PDF_MIME_TYPES);

    assertThat(journeyArtifact).isNotNull();
    assertThat(journeyArtifact.getUrl()).isEqualTo(s3ObjectURL);
    assertThat(journeyArtifact.getSignedUrl()).isEqualTo(s3SignedUrl);
    assertThat(journeyArtifact.getType()).isEqualTo("image");

    ArgumentCaptor<ObjectMetadata> captor = ArgumentCaptor.forClass(ObjectMetadata.class);
    verify(transferManagerMock, times(1))
        .upload(
            Mockito.eq("test_bucket"),
            Mockito.endsWith("originalFile.jpg"),
            any(),
            captor.capture());
    ObjectMetadata objectMetadata = captor.getValue();
    assertThat(objectMetadata).isNotNull();
    assertThat(objectMetadata.getContentType()).isEqualTo("image/jpeg");
  }

  @Test
  void upload_pdfUpload() {
    setupMocksForUpload("resultBucket", "resultKey");

    String testUpload = "Some thing to upload";
    MultipartFile multipartFile =
        new MockMultipartFile("testFile", "originalFile.pdf", "", testUpload.getBytes());
    JourneyArtifact journeyArtifact = artifactService.upload(multipartFile, IMAGE_PDF_MIME_TYPES);

    assertThat(journeyArtifact).isNotNull();
    assertThat(journeyArtifact.getUrl()).isEqualTo(s3ObjectURL);
    assertThat(journeyArtifact.getSignedUrl()).isEqualTo(s3SignedUrl);
    assertThat(journeyArtifact.getType()).isEqualTo("file");

    ArgumentCaptor<ObjectMetadata> captor = ArgumentCaptor.forClass(ObjectMetadata.class);
    verify(transferManagerMock, times(1))
        .upload(
            Mockito.eq("test_bucket"),
            Mockito.endsWith("originalFile.pdf"),
            any(),
            captor.capture());
    ObjectMetadata objectMetadata = captor.getValue();
    assertThat(objectMetadata).isNotNull();
    assertThat(objectMetadata.getContentType()).isEqualTo("application/pdf");
  }

  @Test
  void upload_invalidMimeType() {
    setupMocksForUpload("resultBucket", "resultKey");

    String testUpload = "Some thing to upload";
    MultipartFile multipartFile =
        new MockMultipartFile("testFile", "originalFile.exe", "", testUpload.getBytes());

    try {
      artifactService.upload(multipartFile, IMAGE_PDF_MIME_TYPES);
      fail("no exception thrown");
    } catch (UnsupportedMimetypeException e) {
      // No op
    }

    verify(transferManagerMock, never()).upload(any(), any(), any(), any());
  }

  @Test
  void upload_invalidFile() {
    setupMocksForUpload("resultBucket", "resultKey");
    when(fileCheckerServiceMock.isValidFile(anyString(), any())).thenReturn(false);

    String testUpload = "Some thing to upload";
    MultipartFile multipartFile =
        new MockMultipartFile("testFile", "originalFile.pdf", "", testUpload.getBytes());

    try {
      artifactService.upload(multipartFile, IMAGE_PDF_MIME_TYPES);
      fail("no exception thrown");
    } catch (UnsupportedMimetypeException e) {
      // No op
    }

    verify(transferManagerMock, never()).upload(any(), any(), any(), any());
  }

  @SneakyThrows
  private void setupMocksForUpload(String bucket, String key) {
    Upload uploadMock = mock(Upload.class);
    when(transferManagerMock.upload(any(), any(), any(), any())).thenReturn(uploadMock);
    UploadResult uploadResultMock = mock(UploadResult.class);
    when(uploadMock.waitForUploadResult()).thenReturn(uploadResultMock);
    when(uploadResultMock.getBucketName()).thenReturn(bucket);
    when(uploadResultMock.getKey()).thenReturn(key);

    when(amazonS3Mock.getUrl(bucket, key)).thenReturn(s3ObjectURL);
    when(amazonS3Mock.generatePresignedUrl(any())).thenReturn(s3SignedUrl);

    when(fileCheckerServiceMock.isValidFile(anyString(), any())).thenReturn(true);
  }

  @Test
  void createAccessibleLinks() {
    when(amazonS3Mock.generatePresignedUrl(any())).thenReturn(s3SignedUrl);

    JourneyArtifact journeyArtifact =
        JourneyArtifact.builder().fileName("testFile").url(validS3ObjectUrl).type("file").build();
    artifactService.createAccessibleLinks(journeyArtifact);

    assertThat(journeyArtifact.getSignedUrl()).isEqualTo(s3SignedUrl);

    ArgumentCaptor<GeneratePresignedUrlRequest> captor =
        ArgumentCaptor.forClass(GeneratePresignedUrlRequest.class);
    verify(amazonS3Mock, times(1)).generatePresignedUrl(captor.capture());
    assertThat(captor.getValue().getBucketName()).isEqualTo("test_bucket");
    assertThat(captor.getValue().getKey())
        .isEqualTo("6b784cd1-9c30-4650-a907-b6fd3ccfb4da-original.jpg");
    assertThat(captor.getValue().getMethod()).isEqualTo(HttpMethod.GET);
  }
}
