package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationRequestModel;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Controller
public class DeclarationSubmitController {

  public static final String URL_DECLARATION = "/apply-for-a-badge/declaration";
  public static final String TEMPLATE_DECLARATION = "application-end/declaration";

  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String TEMPLATE_APPLICATION_SUBMITTED = "application-end/submitted";

  @GetMapping(URL_DECLARATION)
  public String show_declaration(
      @Valid @ModelAttribute("formRequest") DeclarationRequestModel formRequest, Model model) {

    return TEMPLATE_DECLARATION;
  }

  @PostMapping(URL_DECLARATION)
  public String submit_declaration(
      @Valid @ModelAttribute("formRequest") DeclarationRequestModel formRequest,
      BindingResult bindingResult,
      Model model) {

    model.addAttribute("errorSummary", new ErrorViewModel());

    if (bindingResult.hasErrors()) {
      return TEMPLATE_DECLARATION;
    }

    return "redirect:" + URL_APPLICATION_SUBMITTED;
  }

  @GetMapping(URL_APPLICATION_SUBMITTED)
  public String show_submitted() {
    return TEMPLATE_APPLICATION_SUBMITTED;
  }
}
