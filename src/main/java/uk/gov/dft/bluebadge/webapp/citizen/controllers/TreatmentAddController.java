package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_LIST;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

@Controller
@RequestMapping(Mappings.URL_TREATMENT_ADD)
public class TreatmentAddController implements StepController {

  private RouteMaster routeMaster;
  private static final String TEMPLATE = "treatment-add";

  @Autowired
  TreatmentAddController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, new TreatmentAddForm());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) TreatmentAddForm treatmentAddForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, treatmentAddForm, bindingResult, attr);
    }

    TreatmentListForm treatmentListForm = journey.getFormForStep(TREATMENT_LIST);
    if (null == treatmentListForm) {
      treatmentListForm = TreatmentListForm.builder().build();
    }

    treatmentListForm.setHasTreatment("yes");
    treatmentListForm.addTreatment(treatmentAddForm);
    journey.setFormForStep(treatmentListForm);

    return "redirect:" + Mappings.URL_TREATMENT_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.TREATMENT_ADD;
  }
}
