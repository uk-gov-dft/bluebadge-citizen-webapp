package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MEDICATION_LIST;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;

@Controller
@RequestMapping(Mappings.URL_MEDICATION_LIST)
public class MedicationListController implements StepController {

  private static final String TEMPLATE = "walking/medication-list";
  private final RouteMaster routeMaster;

  @Autowired
  MedicationListController(RouteMaster routeMaster) {
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
      model.addAttribute(FORM_REQUEST, MedicationListForm.builder().build());
    }
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MedicationListForm medicationListForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, medicationListForm, bindingResult, attr);
    }

    MedicationListForm journeyForm = journey.getOrSetFormForStep(medicationListForm);
    journeyForm.setHasMedication(medicationListForm.getHasMedication());
    if ("no".equals(medicationListForm.getHasMedication())) {
      journeyForm.setMedications(new ArrayList<>());
    } else if (journeyForm.getMedications().isEmpty()) {
      journeyForm.setHasMedication("no");
    }
    journey.setFormForStep(journeyForm);

    // Don't overwrite medications in journey
    // as it is not bound to inputs in ui form and always null on submit

    return routeMaster.redirectToOnSuccess(journeyForm, journey);
  }

  @GetMapping(value = "/remove")
  public String handleDelete(
      @RequestParam(name = "uuid") String uuid,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (journey.hasStepForm(getStepDefinition())
        && null
            != ((MedicationListForm) journey.getFormForStep(getStepDefinition()))
                .getMedications()) {
      ((MedicationListForm) journey.getFormForStep(getStepDefinition()))
          .getMedications()
          .removeIf(item -> item.getId().equals(uuid));
    }

    return "redirect:" + Mappings.URL_MEDICATION_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return MEDICATION_LIST;
  }
}
