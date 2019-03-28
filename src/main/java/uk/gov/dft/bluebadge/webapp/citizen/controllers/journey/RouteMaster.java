package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.TaskConfigurationException;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Component
@Slf4j
public class RouteMaster {

  private final JourneySpecification journeySpecification;

  private static final String REDIRECT = "redirect:";
  public static final String ERROR_SUFFIX = "#error";

  public RouteMaster(JourneySpecification journeySpecification) {
    this.journeySpecification = journeySpecification;
  }

  public String redirectToOnSuccess(StepForm form, Journey journey) {
    StepDefinition nextStep = getNextStep(form, journey);
    return REDIRECT + Mappings.getUrl(nextStep);
  }

  private StepDefinition getNextStep(StepForm form, Journey journey) {
    StepDefinition currentStep = form.getAssociatedStep();
    Task currentTask = journeySpecification.determineTask(journey, currentStep);

    Optional<StepDefinition> nextStepMaybe = form.determineNextStep(journey);
    if (nextStepMaybe.isPresent() && !currentTask.getSteps().contains(nextStepMaybe.get())) {
      throw new TaskConfigurationException(
          "Step form: " + form + ", returned a step not within the task. " + nextStepMaybe);
    }
    return nextStepMaybe.orElseGet(
        () -> journeySpecification.determineNextStep(journey, currentStep));
  }

  public String startingPoint() {
    // TODO Change starting point logic
    // preApplicationTask.getFirstStep()
    return REDIRECT + Mappings.getUrl(StepDefinition.HOME.getDefaultNext());
  }

  public String backToCompletedPrevious() {
    // TODO Go to the Task List. Or first page if pre task not done
    return REDIRECT + Mappings.getUrl(StepDefinition.HOME);
  }

  public String redirectToOnBindingError(
      StepController currentStep,
      Object formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    return redirectToOnBindingError(
        currentStep, formRequest, bindingResult, attr, new ErrorViewModel());
  }

  public String redirectToOnBindingError(
      StepController currentStep,
      Object formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr,
      ErrorViewModel errorViewModel) {
    attr.addFlashAttribute("errorSummary", errorViewModel);
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
      case BADGE_PAYMENT_RETURN:
        return isValidState(StepDefinition.BADGE_PAYMENT, journey);
      case NOT_PAID:
        return isValidState(StepDefinition.BADGE_PAYMENT, journey);
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

    return isValidStateInner(step, journey);
  }

  /**
   * The step is valid, if it is within a task that is part of the current journey and that all the
   * previous steps within the task have been complete.
   *
   * <p>And that the previous section is complete. For example, Pay step is invalid if the pre
   * application and the application sections are incomplete.
   *
   * @param step
   * @param journey
   * @return
   */
  public boolean isValidStateInner(StepDefinition step, Journey journey) {

    Task task = journeySpecification.determineTask(journey, step);

    if (!journeySpecification.arePreviousSectionsComplete(journey, task)) {
      return false;
    }

    StepDefinition currentLoopStep = task.getFirstStep(journey);
    int stepsWalked = 0;
    while (true) {

      if (currentLoopStep == step) {
        // Got to step being validated in journey, so it is valid.
        return true;
      }

      // Should not need next...but don't want an infinite loop.
      if (stepsWalked++ > task.getSteps().size()) {
        log.error("IsValidState journey walk got into infinite loop. Step being checked {}.", step);
        throw new IllegalStateException();
      }
      // Break in the journey, expected only if a guard question has been changed
      // and an attempt to navigate past it happened.
      if (!journey.hasStepForm(currentLoopStep)) {
        return false;
      }

      StepDefinition nextStep;

      StepForm currentLoopForm = journey.getFormForStep(currentLoopStep);
      nextStep = getNextStep(currentLoopForm, journey);
      if (null == nextStep) {
        // Got to the end of the task without finding the step.
        // Error!!
        return false;
      }

      currentLoopStep = nextStep;
    }
  }
}
