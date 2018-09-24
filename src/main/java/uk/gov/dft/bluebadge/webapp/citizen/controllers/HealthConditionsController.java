package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;

@Controller
@RequestMapping(Mappings.URL_HEALTH_CONDITIONS)
public class HealthConditionsController implements StepController {

  private static final String TEMPLATE_HEALTH_CONDITIONS = "health-conditions";

  private final RouteMaster routeMaster;

  @Autowired
  public HealthConditionsController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest") && null != journey.getHealthConditionsForm()) {
      model.addAttribute("formRequest", journey.getHealthConditionsForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", HealthConditionsForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE_HEALTH_CONDITIONS;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") HealthConditionsForm healthConditionsForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, healthConditionsForm, bindingResult, attr);
    }

    journey.setHealthConditionsForm(healthConditionsForm);

    return routeMaster.redirectToOnSuccess(healthConditionsForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HEALTH_CONDITIONS;
  }
}
