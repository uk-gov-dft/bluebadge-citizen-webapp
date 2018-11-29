package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

  public static final String REDIRECT = "redirect:";
  private ObjectMapper objectMapper;

  public ErrorControllerAdvice(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, HttpServletRequest req) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);
    return REDIRECT + ErrorController.URL_500_ERROR;
  }

  @ExceptionHandler(ClientApiException.class)
  public String handleClientApiException(ClientApiException ex, HttpServletRequest req) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);

    try {
      String commonResponse =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ex.getCommonResponse());
      log.warn("Exception common response:{}", commonResponse);
    } catch (JsonProcessingException e) {
      log.warn("Failed to convert common response from exception.", e);
    }
    return REDIRECT + ErrorController.URL_500_ERROR;
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public String handleMaxSizeException(
      MaxUploadSizeExceededException exc,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes attr) {
    log.info("Handling MaxUploadSizeExceededException");

    if (null != request.getAttribute("javax.servlet.error.request_uri")) {
      attr.addFlashAttribute("MAX_FILE_SIZE_EXCEEDED", "true");
      String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
      return REDIRECT + uri;
    }
    log.info("Handling MaxUploadSizeExceededException. Failed to determine the redirect path.");
    return handleException(exc, request);
  }
}
