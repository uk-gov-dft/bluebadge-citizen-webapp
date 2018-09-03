package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  public static final String URL_ROOT = "/";

  @GetMapping(URL_ROOT)
  public String show() {
    return "redirect:/applicant";
  }
}
