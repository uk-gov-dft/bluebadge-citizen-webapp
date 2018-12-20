package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Lists;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice;

public class ErrorControllerAdviceTest {

  @Mock private HttpServletRequest reqMock;
  @Mock private ObjectMapper objectMapperMock;

  @Mock ObjectWriter writerMock;

  private ErrorControllerAdvice controllerAdvice;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controllerAdvice = new ErrorControllerAdvice(objectMapperMock);
  }

  @Test
  public void handleException_shouldReturnRedirectToErrorTemplateAndPopulateRedirectAttributes() {
    Exception ex = new Exception("my error message");

    String template = controllerAdvice.handleException(ex, reqMock);
    assertThat(template).isEqualTo("redirect:/something-went-wrong");
  }

  @Test
  public void handleHttpException_shouldReturnRedirectToErrorTemplateAndPopulateRedirectAttributes()
      throws JsonProcessingException {
    CommonResponse commonResponse = new CommonResponse();
    commonResponse
        .error(
            new Error()
                .message("some message")
                .code(500)
                .reason("no reason")
                .errors(Lists.newArrayList(new ErrorErrors())))
        .id("someId")
        .context("context");

    ClientApiException ex = new ClientApiException(commonResponse);

    when(objectMapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
    when(writerMock.writeValueAsString(ex.getCommonResponse()))
        .thenReturn("some api client error message");

    String template = controllerAdvice.handleClientApiException(ex, reqMock);
    assertThat(template).isEqualTo("redirect:/something-went-wrong");
  }

  @Test
  public void handleMaxSizeException_shouldRedirectToOriginalUrl() {
    MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(1000L);
    RedirectAttributes ra = new RedirectAttributesModelMap();
    when(reqMock.getAttribute("javax.servlet.error.request_uri")).thenReturn("/someUrl");

    String template = controllerAdvice.handleMaxSizeException(ex, reqMock, ra);
    assertThat(template).isEqualTo("redirect:/someUrl");
    assertThat(ra.getFlashAttributes()).isNotNull();
    assertThat(ra.getFlashAttributes().get("MAX_FILE_SIZE_EXCEEDED")).isEqualTo(true);
  }

  @Test
  public void handleMaxSizeException_whenOriginalUrlNotAvailable_shouldRedirectToErrorPage() {
    MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(1000L);
    RedirectAttributes ra = new RedirectAttributesModelMap();
    when(reqMock.getAttribute("javax.servlet.error.request_uri")).thenReturn(null);

    String template = controllerAdvice.handleMaxSizeException(ex, reqMock, ra);
    assertThat(template).isEqualTo("redirect:/something-went-wrong");
    assertThat(ra.getFlashAttributes().get("MAX_FILE_SIZE_EXCEEDED")).isNull();
  }
}
