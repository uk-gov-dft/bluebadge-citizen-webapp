package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

@Controller
@RequestMapping(Mappings.URL_APPLY_IN_WELSH)
public class ApplyInWelshController {

  private static final String TEMPLATE = "apply-in-welsh";

  @Autowired
  public ApplyInWelshController() {}

  @GetMapping
  public String show() {
    return TEMPLATE;
  }
}
