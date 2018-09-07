package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ClientApiException;

//@Profile("dev")
// @ControllerAdvice
@Slf4j
public class DevExceptionHandler {
  @Autowired ObjectMapper objectMapper;

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
  }
}
