package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

@Controller
@RequestMapping(Mappings.URL_MAIN_REASON)
public class MainReasonController implements StepController {
  private static final String TEMPLATE = "mainreason/main-reason";

  private final RouteMaster routeMaster;

  @Autowired
  MainReasonController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, MainReasonForm.builder().build());
    }

    String walkingKey = "options.mainReasonPage.walkd";
    if (Nation.ENG.equals(journey.getNation())) {
      walkingKey = walkingKey + ".england";
    } else if (Nation.SCO.equals(journey.getNation())) {
      walkingKey = walkingKey + ".scotland";
    } else if (Nation.WLS.equals(journey.getNation())) {
      walkingKey = walkingKey + ".wales";
    }

    model.addAttribute(
        "formOptions",
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("mainReasonPage.content.title", journey)
            .addOptionApplicantAware(BLIND, "options.mainReasonPage.blind", journey)
            .addOptionApplicantAware(WALKD, walkingKey, journey)
            .addOptionApplicantAware(
                EligibilityCodeField.TERMILL, "options.mainReasonPage.termill", journey)
            .addOptionApplicantAware(ARMS, "options.mainReasonPage.arms", journey)
            .addOptionApplicantAware(CHILDBULK, "options.mainReasonPage.childbulk", journey)
            .addOptionApplicantAware(CHILDVEHIC, "options.mainReasonPage.childvehic", journey)
            .addOptionApplicantAware(NONE, "options.mainReasonPage.none", journey)
            .build());

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MainReasonForm mainReasonForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, mainReasonForm, bindingResult, attr);
    }

    journey.setFormForStep(mainReasonForm);

    return routeMaster.redirectToOnSuccess(mainReasonForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MAIN_REASON;
  }
}
