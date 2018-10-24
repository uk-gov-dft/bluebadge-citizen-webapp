package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

import javax.validation.Valid;
import java.util.ArrayList;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_REMOVE_PART;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_TREATMENT_LIST)
public class TreatmentListController implements StepController {

  private static final String TEMPLATE = "treatment-list";
  private final RouteMaster routeMaster;

  @Autowired
  public TreatmentListController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      // Create object in journey with empty list.
      // Want to not get any null pointers accessing list.
      journey.setFormForStep(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) TreatmentListForm treatmentListForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, treatmentListForm, bindingResult, attr);
    }

    TreatmentListForm journeyForm = journey.getFormForStep(TREATMENT_LIST);
    // Reset if no selected
    // Treat as No selected if no aids added whilst yes was selected
    if ("no".equals(treatmentListForm.getHasTreatment())
        || ("yes".equals(treatmentListForm.getHasTreatment())
            && journeyForm.getTreatments().isEmpty())) {
      journey.setFormForStep(
          TreatmentListForm.builder().hasTreatment("no").treatments(new ArrayList<>()).build());
    } else {
      journeyForm.setHasTreatment(treatmentListForm.getHasTreatment());
    }

    // Don't overwrite treatmentList in journey
    // as it is not bound to inputs in ui form and always null on submit

    return routeMaster.redirectToOnSuccess(treatmentListForm);
  }

  @GetMapping(value = URL_REMOVE_PART)
  public String handleDelete(
      @RequestParam(name = "uuid") String uuid,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (journey.hasStepForm(TREATMENT_LIST)
        && null != ((TreatmentListForm) journey.getFormForStep(TREATMENT_LIST)).getTreatments()) {
      ((TreatmentListForm) journey.getFormForStep(TREATMENT_LIST))
          .getTreatments()
          .removeIf(item -> item.getId().equals(uuid));
    }

    return "redirect:" + Mappings.URL_TREATMENT_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return TREATMENT_LIST;
  }
}
