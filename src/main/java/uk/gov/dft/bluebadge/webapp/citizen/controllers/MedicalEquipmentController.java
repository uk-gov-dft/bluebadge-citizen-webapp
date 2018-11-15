package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.*;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MedicalEquipmentForm;

@Controller
@RequestMapping(Mappings.URL_MEDICAL_EQUIPMENT)
public class MedicalEquipmentController implements StepController {

  private static final String TEMPLATE = "medical-equipment";

  private final RouteMaster routeMaster;

  @Autowired
  public MedicalEquipmentController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, MedicalEquipmentForm.builder().build());
    }

    // Otherwise, is redirect from post with binding errors.

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MedicalEquipmentForm form,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, form, bindingResult, attr);
    }

    journey.setFormForStep(form);

    return routeMaster.redirectToOnSuccess(form);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MEDICAL_EQUIPMENT;
  }

  @ModelAttribute("equipment")
  public RadioOptionsGroup equipment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return null;
    }

    List<RadioOption> options = new ArrayList<>();
    options.add(new RadioOption(VENT.name(), "medicalEquipment.option.vent"));
    options.add(new RadioOption(SUCTION.name(), "medicalEquipment.option.suction"));
    options.add(new RadioOption(PUMP.name(), "medicalEquipment.option.pump"));
    options.add(new RadioOption(PARENT.name(), "medicalEquipment.option.parent"));
    options.add(new RadioOption(SYRINGE.name(), "medicalEquipment.option.syringe"));
    options.add(new RadioOption(OXYADMIN.name(), "medicalEquipment.option.oxyadmin"));
    options.add(new RadioOption(OXYSAT.name(), "medicalEquipment.option.oxysat"));
    options.add(new RadioOption(CAST.name(), "medicalEquipment.option.cast"));
    options.add(new RadioOption(OTHER.name(), "medicalEquipment.option.other"));

    return new RadioOptionsGroup(journey.who + "medicalEquipment.title", options);
  }
}
