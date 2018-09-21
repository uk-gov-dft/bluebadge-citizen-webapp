package uk.gov.dft.bluebadge.webapp.citizen.controllers.advice;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalBindingAdvice {
  @InitBinder
  public void customizeBinding(WebDataBinder binder) {
    binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
  }
}
