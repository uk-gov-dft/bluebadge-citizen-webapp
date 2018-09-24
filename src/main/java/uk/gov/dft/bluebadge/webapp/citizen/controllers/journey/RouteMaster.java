package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Component
public class RouteMaster {

  public String redirectToOnSuccess(StepController currentStep) {

    StepDefinition nextStep =
        currentStep
            .getStepDefinition()
            .getNext()
            .stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException("Failed to determine next step for: " + currentStep));

    return "redirect:" + Mappings.getUrl(nextStep);
  }

  public String backToCompletedPrevious() {
    return "redirect:" + Mappings.getUrl(StepDefinition.HOME);
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
    return "redirect:" + Mappings.getUrl(currentStep.getStepDefinition());
  }
}
