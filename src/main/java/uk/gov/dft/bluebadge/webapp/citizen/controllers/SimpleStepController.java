package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public abstract class SimpleStepController implements StepController {

  protected RouteMaster routeMaster;

  protected abstract String getTemplate();

  protected abstract StepForm getNewFormInstance();

  public String show(Journey journey, Model model) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, getNewFormInstance());
    }

    return getTemplate();
  }

  public String submit(
      Journey journey, StepForm stepForm, BindingResult bindingResult, RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, stepForm, bindingResult, attr);
    }

    journey.setFormForStep(stepForm);

    return routeMaster.redirectToOnSuccess(stepForm, journey);
  }
}
