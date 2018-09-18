package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChooseYourCouncilController {

  public static final String TEMPLATE = "choose-council";

  public ChooseYourCouncilController() {}

  @GetMapping("choose-council")
  public String show() {
    return TEMPLATE;
  }
}
