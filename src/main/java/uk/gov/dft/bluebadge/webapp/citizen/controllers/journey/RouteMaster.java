package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.InvalidStateForJourneyException;
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
    StepDefinition currentStep = form.getAssociatedStep();
    try {
      return redirectToOnSuccess(currentStep, journey);
    } catch (InvalidStateForJourneyException e) {
      // The controllers set the form before bypassing. So ignore the current step.
      // Only possibility of error would be the first step. But an error on the first step!?
      if (!journey.isEmptyJourney(currentStep)) {
        log.error("Invalid state when redirecting from state:{}.", currentStep, e);
      } else {
        log.info(
            "Invalid state when redirecting from state:{}. Exception:{}",
            currentStep,
            e.toString());
      }
      return REDIRECT + Mappings.getUrl(StepDefinition.TASK_LIST);
    }
  }

  public String redirectToOnSuccess(StepDefinition currentStep, Journey journey) {
    StepDefinition nextStep = getNextStep(currentStep, journey);
    if (null == nextStep) {
      nextStep = StepDefinition.TASK_LIST;
    }
    return REDIRECT + Mappings.getUrl(nextStep);
  }

  private StepDefinition getNextStep(StepDefinition currentStep, Journey journey) {
    return journeySpecification.determineNextStep(journey, currentStep);
  }

  public String startingPoint() {
    // TODO Change starting point logic
    // preApplicationTask.getFirstStep()
    return REDIRECT + Mappings.getUrl(StepDefinition.HOME.getDefaultNext());
  }

  public String backToCompletedPrevious(Journey journey) {
    return journeySpecification.getPreApplicationJourney().isComplete(journey)
        ? REDIRECT + Mappings.getUrl(StepDefinition.TASK_LIST)
        : REDIRECT + Mappings.getUrl(StepDefinition.HOME);
  }

  public String redirectToOnBindingError(
      StepController currentStep,
      Object formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    return redirectToOnBindingError(
        Mappings.getUrl(currentStep.getStepDefinition()), formRequest, bindingResult, attr);
  }

  public static String redirectToOnBindingError(
      String url, Object formRequest, BindingResult bindingResult, RedirectAttributes attr) {
    attr.addFlashAttribute("errorSummary", new ErrorViewModel());
    attr.addFlashAttribute(
        "org.springframework.validation.BindingResult.formRequest", bindingResult);
    attr.addFlashAttribute("formRequest", formRequest);
    return REDIRECT + url + ERROR_SUFFIX;
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

    Task task;
    try {
      task = journeySpecification.determineTask(journey, step);
    } catch (InvalidStateForJourneyException e) {
      return false;
    }

    if (!journeySpecification.arePreviousSectionsComplete(journey, task)) {
      return false;
    }

    // Final section is sequential
    JourneySection journeySection = journeySpecification.determineSection(journey, step);
    return journeySection == journeySpecification.getSubmitAndPayJourney()
        ? sequenceOrderValid(journeySection, task, step, journey)
        : anyOrderValid(task, step, journey);
  }

  /** All tasks prior to the current one must be complete */
  private boolean sequenceOrderValid(
      JourneySection journeySection, Task task, StepDefinition step, Journey journey) {
    for (Task priorTask : journeySection.getTasks()) {
      if (task.equals(priorTask)) break;
      if (!priorTask.isComplete(journey)) return false;
    }

    return task.isValidStep(journey, step);
  }

  private boolean anyOrderValid(Task task, StepDefinition step, Journey journey) {
    return task.isValidStep(journey, step);
  }
}
