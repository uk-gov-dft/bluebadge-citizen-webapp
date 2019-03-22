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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;

@Controller
@RequestMapping(Mappings.URL_WALKING_TIME)
public class WalkingTimeController implements StepController {

  private static final String TEMPLATE = "walking/walking-time";

  private final RouteMaster routeMaster;

  @Autowired
  WalkingTimeController(RouteMaster routeMaster) {
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
      model.addAttribute(FORM_REQUEST, WalkingTimeForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) WalkingTimeForm walkingTimeForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, walkingTimeForm, bindingResult, attr);
    }

    journey.setFormForStep(walkingTimeForm);

    return routeMaster.redirectToOnSuccess(walkingTimeForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.WALKING_TIME;
  }

  @ModelAttribute("walkingTimeOptions")
  public RadioOptionsGroup walkingTimeOptions(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return null;
    }

    List<RadioOption> options = new ArrayList<>();
    options.add(
        new RadioOption(
            WalkingLengthOfTimeCodeField.CANTWALK.name(),
            journey.getWho() + "walkingTime.select.option.CANTWALK"));

    options.add(
        new RadioOption(
            WalkingLengthOfTimeCodeField.LESSMIN.name(), "walkingTime.select.option.LESSMIN"));

    options.add(
        new RadioOption(
            WalkingLengthOfTimeCodeField.FEWMIN.name(), "walkingTime.select.option.FEWMIN"));

    options.add(
        new RadioOption(
            WalkingLengthOfTimeCodeField.MORETEN.name(), "walkingTime.select.option.MORETEN"));

    String selectTitle = journey.getWho() + journey.getWalkingAid() + "walkingTime.select.title";
    return new RadioOptionsGroup(selectTitle, options);
  }
}
