package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

@Controller
@RequestMapping(Mappings.URL_HEALTHCARE_PROFESSIONALS_ADD)
public class HealthcareProfessionalAddController implements StepController {

  private RouteMaster routeMaster;
  private static final String TEMPLATE = "healthcare-professional-add";

  @Autowired
  HealthcareProfessionalAddController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    // Can hit add link before previous form submitted.
    if (!journey.hasStepForm(HEALTHCARE_PROFESSIONAL_LIST)
        || null
            == ((HealthcareProfessionalListForm)
                    journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST))
                .getHealthcareProfessionals()) {
      journey.setFormForStep(
          HealthcareProfessionalListForm.builder()
              .healthcareProfessionals(new ArrayList<>())
              .build());
    }

    ((HealthcareProfessionalListForm) journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST))
        .setHasHealthcareProfessional("yes");

    // On returning to form (binding errors), take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, new HealthcareProfessionalAddForm());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST)
          HealthcareProfessionalAddForm healthcareProfessionalAddForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, healthcareProfessionalAddForm, bindingResult, attr);
    }

    ((HealthcareProfessionalListForm) journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST))
        .getHealthcareProfessionals()
        .add(healthcareProfessionalAddForm);

    return "redirect:" + Mappings.URL_HEALTHCARE_PROFESSIONALS_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HEALTHCARE_PROFESSIONALS_ADD;
  }
}
