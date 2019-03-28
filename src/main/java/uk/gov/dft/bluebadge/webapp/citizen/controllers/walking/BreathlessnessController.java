package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BreathlessnessCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.BreathlessnessForm;

@Controller
@RequestMapping(Mappings.URL_BREATHLESS)
public class BreathlessnessController implements StepController {

  private static final String TEMPLATE = "walking/breathlessness";

  private final RouteMaster routeMaster;

  @Autowired
  BreathlessnessController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, BreathlessnessForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) BreathlessnessForm breathlessnessForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, breathlessnessForm, bindingResult, attr);
    }

    journey.setFormForStep(breathlessnessForm);

    return routeMaster.redirectToOnSuccess(breathlessnessForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.BREATHLESSNESS;
  }

  @ModelAttribute("breathlessnessTypes")
  public RadioOptionsGroup breathlessnessTypes(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return null;
    }

    List options = new ArrayList();
    options.add(
        new RadioOption(
            BreathlessnessCodeField.UPHILL.name(), "breathlessnessPage.select.option.UPHILL"));
    options.add(
        new RadioOption(
            BreathlessnessCodeField.KEEPUP.name(), "breathlessnessPage.select.option.KEEPUP"));
    options.add(
        new RadioOption(
            BreathlessnessCodeField.OWNPACE.name(),
            journey.getWho() + "breathlessnessPage.select.option.OWNPACE"));
    options.add(
        new RadioOption(
            BreathlessnessCodeField.DRESSED.name(),
            journey.getWho() + "breathlessnessPage.select.option.DRESSED"));
    options.add(
        new RadioOption(
            BreathlessnessCodeField.OTHER.name(),
            journey.getWho() + "breathlessnessPage.select.option.OTHER"));

    return new RadioOptionsGroup("breathlessnessPage.select.title", options);
  }
}
