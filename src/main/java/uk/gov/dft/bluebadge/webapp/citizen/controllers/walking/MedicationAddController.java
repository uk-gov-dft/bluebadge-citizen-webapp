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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;

@Controller
@RequestMapping(Mappings.URL_MEDICATION_ADD)
public class MedicationAddController implements StepController {

  private RouteMaster routeMaster;
  private static final String TEMPLATE = "walking/medication-add";

  @Autowired
  MedicationAddController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // Can hit add link before previous form submitted.
    if (!journey.hasStepForm(MEDICATION_LIST)
        || null
            == ((MedicationListForm) journey.getFormForStep(MEDICATION_LIST)).getMedications()) {
      journey.setFormForStep(MedicationListForm.builder().medications(new ArrayList<>()).build());
    }

    ((MedicationListForm) journey.getFormForStep(MEDICATION_LIST)).setHasMedication("yes");

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, new MedicationAddForm());
    }

    RadioOptionsGroup radioOptions =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("medication.prescribed.title", journey)
            .withYesNoOptions()
            .build();

    model.addAttribute("yesNoRadioOptions", radioOptions);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MedicationAddForm medicationAddForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, medicationAddForm, bindingResult, attr);
    }

    ((MedicationListForm) journey.getFormForStep(MEDICATION_LIST))
        .getMedications()
        .add(medicationAddForm);

    return "redirect:" + Mappings.URL_MEDICATION_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MEDICATION_ADD;
  }
}
