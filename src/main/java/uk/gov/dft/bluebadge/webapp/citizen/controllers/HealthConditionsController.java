package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

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
  public String show(
      @ModelAttribute("formRequest") HealthConditionsForm healthConditionsForm,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (null != journey.getHealthConditionsForm()) {
      BeanUtils.copyProperties(journey.getHealthConditionsForm(), healthConditionsForm);
    }
    // TODO formRequest is hard coded into errors fragment.
    // 1 is valid to be here else last place from stack
    // 1.1 will need to know what steps are complete + prerequisites
    // 1.2 If not valid redirect to top of stack
    // 2 is back button press or bookmark backwards
    // 2.1 Clean up stack back to here?
    // 3 if all ok Add this page to stack
    return TEMPLATE_HEALTH_CONDITIONS;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") HealthConditionsForm healthConditionsForm,
      BindingResult bindingResult,
      Model model) {

    // 1 Check results.
    // 2 If errors populate them and return to view
    // 3 No errors store results
    // 4 No errors, Get possible redirects and pick and redirect?
    if (bindingResult.hasErrors()) {
      model.addAttribute("errorSummary", new ErrorViewModel());
      return TEMPLATE_HEALTH_CONDITIONS;
    }

    journey.setHealthConditionsForm(healthConditionsForm);

    return routeMaster.redirectToOnSuccess(this);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HEALTH_CONDITIONS;
  }
}
