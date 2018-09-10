package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

  @Autowired ObjectMapper objectMapper;

  @ExceptionHandler(Exception.class)
  public String handleException(
      Exception ex, HttpServletRequest req, RedirectAttributes redirectAttributes) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);
    redirectAttributes.addFlashAttribute("exception", ex);
    return "redirect:" + ErrorController.URL_500_ERROR;
  }

  @ExceptionHandler(ClientApiException.class)
  public String handleHttpException(
      ClientApiException ex, RedirectAttributes redirectAttributes, HttpServletRequest req) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);

    try {
      String commonResponse =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ex.getCommonResponse());
      log.warn("Exception common response:{}", commonResponse);
      redirectAttributes.addFlashAttribute("commonResponse", commonResponse);
    } catch (JsonProcessingException e) {
      log.warn("Failed to convert common response from exception.", e);
    }

    redirectAttributes.addFlashAttribute("exception", ex);
    return "redirect:" + ErrorController.URL_500_ERROR;
  }
}
