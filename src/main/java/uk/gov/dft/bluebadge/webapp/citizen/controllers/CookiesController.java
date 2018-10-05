package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

@Controller
public class CookiesController {

  public static final String TEMPLATE = "cookies";

  @GetMapping(Mappings.URL_COOKIES)
  public String show() {
    return TEMPLATE;
  }
}
