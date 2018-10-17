package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

@Controller
public class PrivacyController {

  public static final String TEMPLATE = "privacy";

  @GetMapping(Mappings.URL_PRIVACY)
  public String show() {
    return TEMPLATE;
  }
}
