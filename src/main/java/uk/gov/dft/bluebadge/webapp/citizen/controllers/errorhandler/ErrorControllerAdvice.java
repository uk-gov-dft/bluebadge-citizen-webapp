package uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

  // Dev
  //@Autowired ObjectMapper objectMapper;

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, HttpServletRequest req, RedirectAttributes redirectAttributes) {
    //String message = ex.getClass().getName();
    //logException(message, ex);
    //redirectAttributes.addFlashAttribute("exception", ex);
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);

    return "redirect:" + ErrorController.URL_500_ERROR;
  }

  @ExceptionHandler(ClientApiException.class)
  public String handleHttpException(ClientApiException ex, RedirectAttributes redirectAttributes) {
    //String message = ex.getClass().getName();
    //logException(message, ex);
    //redirectAttributes.addFlashAttribute("exception", ex);

    return "redirect:" + ErrorController.URL_500_ERROR;
  }

  private void logException(String title, Exception ex) {
    log.debug(title + " exception, exception [()], details [()]", ex.getMessage(), ex);
  }

  // Dev
  /*@Profile("dev")
  @ExceptionHandler
  public ModelAndView handleError(HttpServletRequest req, ClientApiException ex) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);

    ModelAndView mav = new ModelAndView();
    try {
      String commonResponse =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ex.getCommonResponse());
      log.warn("Exception common response:{}", commonResponse);
      mav.addObject("commonResponse", commonResponse);
    } catch (JsonProcessingException e) {
      log.warn("Failed to convert common response from exception.", e);
    }
    mav.addObject("exception", ex);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("dev-error");
    return mav;
  }

  @ExceptionHandler
  public ModelAndView handleError(HttpServletRequest req, Exception ex) {
    log.error("Request: {} raised {}.", req.getRequestURL(), ex.toString(), ex);

    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", ex);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("dev-error");
    return mav;
  }*/
}
