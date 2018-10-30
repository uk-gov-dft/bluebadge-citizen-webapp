package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveBenefitForm;

@Controller
@RequestMapping(Mappings.URL_PROVE_BENEFIT)
public class ProveBenefitController implements StepController {

  public static final String TEMPLATE = "prove-benefit";
  public static final String FORM_REQUEST = "formRequest";

  private final RouteMaster routeMaster;

  @Autowired
  public ProveBenefitController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getProveBenefitForm()) {
      model.addAttribute(FORM_REQUEST, journey.getProveBenefitForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ProveBenefitForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ProveBenefitForm proveBenefitForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (proveBenefitForm.getHasProof() != null
        && !proveBenefitForm.getHasProof()
        && proveBenefitForm.getAwardEndDate().isDatePartMissing()) {
      bindingResult.rejectValue("awardEndDate", "ConditionalNotNull.awardEndDate");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, proveBenefitForm, bindingResult, attr);
    }

    journey.setProveBenefitForm(proveBenefitForm);

    return routeMaster.redirectToOnSuccess(proveBenefitForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PROVE_BENEFIT;
  }
}
