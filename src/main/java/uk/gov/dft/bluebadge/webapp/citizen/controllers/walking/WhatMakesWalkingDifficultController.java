package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.BBValidationUtils.NOT_BLANK;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.BBValidationUtils.NOT_NULL;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.BBValidationUtils.SIZE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.BBValidationUtils;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

@Controller
@RequestMapping(Mappings.URL_WHAT_MAKES_WALKING_DIFFICULT)
public class WhatMakesWalkingDifficultController implements StepController {

  private static final String TEMPLATE = "walking/what-makes-walking-difficult";
  private static final int DESC_MAX_LENGTH = 2000;

  private final RouteMaster routeMaster;

  @Autowired
  WhatMakesWalkingDifficultController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, WhatMakesWalkingDifficultForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST)
          WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    validateForm(whatMakesWalkingDifficultForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, whatMakesWalkingDifficultForm, bindingResult, attr);
    }

    journey.setFormForStep(whatMakesWalkingDifficultForm);

    return routeMaster.redirectToOnSuccess(whatMakesWalkingDifficultForm, journey);
  }

  private void validateForm(
      WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm, BindingResult bindingResult) {
    List<WalkingDifficultyTypeCodeField> types =
        whatMakesWalkingDifficultForm.getWhatWalkingDifficulties();
    if (null != types) {
      if (types.contains(WalkingDifficultyTypeCodeField.PAIN)) {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "painDescription", NOT_BLANK);
        BBValidationUtils.rejectIfTooLong(bindingResult, "painDescription", SIZE, DESC_MAX_LENGTH);
      }
      if (types.contains(WalkingDifficultyTypeCodeField.BALANCE)) {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "balanceDescription", NOT_BLANK);
        BBValidationUtils.rejectIfTooLong(
            bindingResult, "balanceDescription", SIZE, DESC_MAX_LENGTH);
        ValidationUtils.rejectIfEmptyOrWhitespace(
            bindingResult, "healthProfessionsForFalls", NOT_NULL);
      }
      if (types.contains(WalkingDifficultyTypeCodeField.DANGER)) {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "dangerousDescription", NOT_BLANK);
        BBValidationUtils.rejectIfTooLong(
            bindingResult, "dangerousDescription", SIZE, DESC_MAX_LENGTH);
        ValidationUtils.rejectIfEmptyOrWhitespace(
            bindingResult, "chestLungHeartEpilepsy", NOT_NULL);
      }
      if (types.contains(WalkingDifficultyTypeCodeField.SOMELSE)) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
            bindingResult, "somethingElseDescription", NOT_BLANK);
        BBValidationUtils.rejectIfTooLong(
            bindingResult, "somethingElseDescription", SIZE, DESC_MAX_LENGTH);
      }
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.WHAT_MAKES_WALKING_DIFFICULT;
  }

  @ModelAttribute("walkingDifficulties")
  public RadioOptionsGroup walkingDifficulties(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return null;
    }

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
            WalkingDifficultyTypeCodeField.DANGER.name(),
            journey.getWho()
                + journey.getNation().name()
                + ".whatMakesWalkingDifficult.select.option.dangerous"));
    if (Nation.SCO.equals(journey.getNation()) || Nation.WLS.equals(journey.getNation())) {
      options.add(
          new RadioOption(
              WalkingDifficultyTypeCodeField.STRUGGLE.name(),
              journey.getWho() + "whatMakesWalkingDifficult.select.option.struggle"));
    }
    options.add(
        new RadioOption(
            WalkingDifficultyTypeCodeField.SOMELSE.name(),
            "whatMakesWalkingDifficult.select.option.somethingElse"));

    return new RadioOptionsGroup("whatMakesWalkingDifficult.select.title", options);
  }
}
