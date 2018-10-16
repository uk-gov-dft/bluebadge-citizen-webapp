package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Component
public class RouteMaster {

  private static final String REDIRECT = "redirect:";

  public String redirectToOnSuccess(StepForm form, Journey journey) {
    StepDefinition currentStep = form.getAssociatedStep();

    StepDefinition nextStep =
        form.determineNextStep(journey).orElseGet(currentStep::getDefaultNext);

    if (!currentStep.getNext().contains(nextStep)) {
      throw new IllegalStateException(
          "Next step '" + nextStep + "', not associated with the current one:" + currentStep);
    }

    return REDIRECT + Mappings.getUrl(nextStep);
  }

  public String redirectToOnSuccess(StepForm form) {
    StepDefinition currentStep = form.getAssociatedStep();

    StepDefinition nextStep = form.determineNextStep().orElseGet(currentStep::getDefaultNext);

    if (!currentStep.getNext().contains(nextStep)) {
      throw new IllegalStateException(
          "Next step '" + nextStep + "', not associated with the current one:" + currentStep);
    }

    return REDIRECT + Mappings.getUrl(nextStep);
  }

  public String redirectToOnSuccess(StepDefinition currentStep) {
    StepDefinition nextStep = currentStep.getDefaultNext();
    return REDIRECT + Mappings.getUrl(nextStep);
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
    return REDIRECT + Mappings.getUrl(currentStep.getStepDefinition()) + "#error";
  }
}
