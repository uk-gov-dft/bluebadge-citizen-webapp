package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

  private ObjectMapper objectMapper;

  public ErrorControllerAdvice(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, HttpServletRequest req) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);
    return "redirect:" + ErrorController.URL_500_ERROR;
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
    return "redirect:" + ErrorController.URL_500_ERROR;
  }
}
