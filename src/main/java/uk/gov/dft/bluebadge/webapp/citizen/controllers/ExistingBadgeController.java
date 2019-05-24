package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

@Controller
public class ExistingBadgeController implements StepController {

  private static final String TEMPLATE = "existing-badge";
  private static final String EXISTING_BADGE_BYPASS_URL = "/existing-badge-bypass";

  private final RouteMaster routeMaster;

  @Autowired
  ExistingBadgeController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_EXISTING_BADGE)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ExistingBadgeForm.builder().build());
    }

    return TEMPLATE;
  }

  @GetMapping(EXISTING_BADGE_BYPASS_URL)
  public String formByPass(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    ExistingBadgeForm formRequest = ExistingBadgeForm.builder().hasExistingBadge(true).build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @PostMapping(Mappings.URL_EXISTING_BADGE)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ExistingBadgeForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (Boolean.TRUE.equals(formRequest.getHasExistingBadge())) {

      String badgeNum =
          formRequest.hasBadgeNumber() ? formRequest.getBadgeNumber().replaceAll("\\s+", "") : "";

      if (badgeNum.isEmpty() || badgeNum.length() < 6) {
        bindingResult.rejectValue("badgeNumber", "badgeNumber.NotBlank");
      }
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setFormForStep(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.EXISTING_BADGE;
  }
}
