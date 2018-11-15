package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Component
@Slf4j
public class RouteMaster {

  private static final String REDIRECT = "redirect:";
  public static final String ERROR_SUFFIX = "#error";

  public String redirectToOnSuccess(StepForm form) {
    return redirectToOnSuccess(form, null);
  }

  public String redirectToOnSuccess(StepForm form, Journey journey) {
    StepDefinition nextStep = getNextStep(form, journey);
    return REDIRECT + Mappings.getUrl(nextStep);
  }

  private StepDefinition getNextStep(StepForm form, Journey journey) {
    StepDefinition currentStep = form.getAssociatedStep();
    Optional<StepDefinition> nextStep = form.determineNextStep();
    if (!nextStep.isPresent()) {
      nextStep = form.determineNextStep(journey);
    }
    if (!nextStep.isPresent()) {
      nextStep = Optional.of(form.getAssociatedStep().getDefaultNext());
    }

    if (!currentStep.getNext().contains(nextStep.get())) {
      throw new IllegalStateException(
          "Next step '" + nextStep + "', not associated with the current one:" + currentStep);
    }
    return nextStep.get();
  }

  public String startingPoint() {
    return REDIRECT + Mappings.getUrl(StepDefinition.HOME.getDefaultNext());
  }

  public String backToCompletedPrevious() {
    return REDIRECT + Mappings.getUrl(StepDefinition.HOME);
  }

  public String redirectToOnBindingError(
      StepController currentStep,
      Object formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    attr.addFlashAttribute("errorSummary", new ErrorViewModel());
    attr.addFlashAttribute(
        "org.springframework.validation.BindingResult.formRequest", bindingResult);
    attr.addFlashAttribute("formRequest", formRequest);
    return REDIRECT + Mappings.getUrl(currentStep.getStepDefinition()) + ERROR_SUFFIX;
  }

  public boolean isValidState(StepDefinition step, Journey journey) {

    // Custom step validation
    switch (step) {
        // The add pages don't link together in the step definition
        // in the same way as would have created loop with the list
        // pages.  Check the list pages prerequisites
      case MOBILITY_AID_ADD:
        return isValidState(StepDefinition.MOBILITY_AID_LIST, journey);
      case TREATMENT_ADD:
        return isValidState(StepDefinition.TREATMENT_LIST, journey);
      case HEALTHCARE_PROFESSIONALS_ADD:
        return isValidState(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST, journey);
      case MEDICATION_ADD:
        return isValidState(StepDefinition.MEDICATION_LIST, journey);
      case HOME:
        return true;
      default:
    }

    // First step always valid.
    if (StepDefinition.getFirstStep() == step) {
      return true;
    }

    // Replay the journey to find any gaps up to step
    StepForm form = journey.getFormForStep(StepDefinition.getFirstStep());
    if (null == form) return false;

    StepDefinition currentLoopStep = form.getAssociatedStep();
    int stepsWalked = 0;
    // Walk the journey up to step being checked.
    while (true) {

      if (currentLoopStep == step) {
        // Got to step being validated in journey, so it is valid.
        return true;
      }

      // Should not need next...but don't want an infinite loop.
      if (stepsWalked++ > StepDefinition.values().length) {
        log.error("IsValidState journey walk got into infinite loop. Step being checked {}.", step);
        throw new IllegalStateException();
      }
      // Break in the journey, expected only if a guard question has been changed
      // and an attempt to navigate past it happened.
      if (!journey.hasStepForm(currentLoopStep)) return false;

      StepDefinition nextStep;
      if (currentLoopStep.getNext().size() == 0) {
        // Got to end of journey and did not hit step being validated.
        // So the url requested is for a step invalid in this journey.
        return false;
      } else {
        // Get next step.
        StepForm currentLoopForm = journey.getFormForStep(currentLoopStep);
        nextStep = getNextStep(currentLoopForm, journey);
      }

      currentLoopStep = nextStep;
    }
  }
}
