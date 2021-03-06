package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_REMOVE_PART;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

@Controller
@RequestMapping(Mappings.URL_HEALTHCARE_PROFESSIONALS_LIST)
public class HealthcareProfessionalListController implements StepController {

  private static final String TEMPLATE = "healthcare-professional-list";
  private final RouteMaster routeMaster;

  @Autowired
  HealthcareProfessionalListController(RouteMaster routeMaster) {
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

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, HealthcareProfessionalListForm.builder().build());
    }
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST)
          HealthcareProfessionalListForm healthcareProfessionalListForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, healthcareProfessionalListForm, bindingResult, attr);
    }

    HealthcareProfessionalListForm journeyForm =
        journey.getOrSetFormForStep(healthcareProfessionalListForm);
    journeyForm.setHasHealthcareProfessional(
        healthcareProfessionalListForm.getHasHealthcareProfessional());
    if ("no".equals(healthcareProfessionalListForm.getHasHealthcareProfessional())) {
      journeyForm.setHealthcareProfessionals(new ArrayList<>());
    } else if (journeyForm.getHealthcareProfessionals().isEmpty()) {
      journeyForm.setHasHealthcareProfessional("no");
    }
    journey.setFormForStep(journeyForm);

    // Don't overwrite healthcareProfessionals in journey
    // as it is not bound to inputs in ui form and always null on submit
    return routeMaster.redirectToOnSuccess(healthcareProfessionalListForm, journey);
  }

  @GetMapping(value = URL_REMOVE_PART)
  public String handleDelete(
      @RequestParam(name = "uuid") String uuid,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (journey.hasStepForm(getStepDefinition())
        && null
            != ((HealthcareProfessionalListForm) journey.getFormForStep(getStepDefinition()))
                .getHealthcareProfessionals()) {
      ((HealthcareProfessionalListForm) journey.getFormForStep(getStepDefinition()))
          .getHealthcareProfessionals()
          .removeIf(item -> item.getId().equals(uuid));
    }

    return "redirect:" + Mappings.URL_HEALTHCARE_PROFESSIONALS_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return HEALTHCARE_PROFESSIONAL_LIST;
  }
}
