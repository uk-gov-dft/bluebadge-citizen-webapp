package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

@Controller
@RequestMapping(Mappings.URL_WHAT_WALKING_DIFFICULT)
public class WhatWalkingDifficultiesController implements StepController {

  private static final String TEMPLATE = "walking/what-walking-difficult";

  private final RouteMaster routeMaster;

  @Autowired
  public WhatWalkingDifficultiesController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest")
        && null != journey.getWhatMakesWalkingDifficultForm()) {
      model.addAttribute("formRequest", journey.getWhatMakesWalkingDifficultForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", WhatMakesWalkingDifficultForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest")
          WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, whatMakesWalkingDifficultForm, bindingResult, attr);
    }

    journey.setWhatMakesWalkingDifficultForm(whatMakesWalkingDifficultForm);

    return routeMaster.redirectToOnSuccess(whatMakesWalkingDifficultForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.WHAT_WALKING_DIFFICULTIES;
  }

  @ModelAttribute("walkingDifficulties")
  public RadioOptionsGroup walkingDifficulties(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    List options = new ArrayList();
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.PAIN.name(),
            "whatMakesWalkingDifficult.select.option.pain"));
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.BREATH.name(),
            "whatMakesWalkingDifficult.select.option.breathless"));
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.BALANCE.name(),
            "whatMakesWalkingDifficult.select.option.balance"));
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.STRUGGLE.name(),
            journey.who + "whatMakesWalkingDifficult.select.option.longtime"));
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.DANGER.name(),
            journey.who + "whatMakesWalkingDifficult.select.option.dangerous"));
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.SOMELSE.name(),
            "whatMakesWalkingDifficult.select.option.somethingElse"));

    return new RadioOptionsGroup("whatMakesWalkingDifficult.select.title", options);
  }
}
