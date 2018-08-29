package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, RedirectAttributes redirectAttributes) {
    String message = ex.getClass().getName();
    logException(message, ex);
    redirectAttributes.addFlashAttribute("exception", ex);

    return ErrorController.REDIRECT_URL_ERROR;
  }

  private void logException(String title, Exception ex) {
    log.debug(title + " exception, exception [()], details [()]", ex.getMessage(), ex);
  }
}
