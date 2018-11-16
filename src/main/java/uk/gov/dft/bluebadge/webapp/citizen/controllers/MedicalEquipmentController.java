package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.*;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MedicalEquipmentForm;

@Controller
@RequestMapping(Mappings.URL_MEDICAL_EQUIPMENT)
public class MedicalEquipmentController extends SimpleStepController {

  private static final String TEMPLATE = "medical-equipment";

  @Autowired
  public MedicalEquipmentController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  @Override
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    return super.show(journey, model);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MedicalEquipmentForm form,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    validate(form, bindingResult);

    return super.submit(journey, form, bindingResult, attr);
  }

  private void validate(MedicalEquipmentForm form, BindingResult bindingResult) {
    if (null != form.getEquipment()
        && form.getEquipment().contains(OTHER)
        && StringUtils.isBlank(form.getOtherDescription())) {
      bindingResult.rejectValue("otherDescription", "NotBlank");
    }
    if (!StringUtils.isBlank(form.getOtherDescription())
        && form.getOtherDescription().length() > 100) {
      bindingResult.rejectValue("otherDescription", "Size");
    }
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

  @Override
  protected String getTemplate() {
    return TEMPLATE;
  }

  @Override
  protected StepForm getNewFormInstance() {
    return MedicalEquipmentForm.builder().build();
  }
}
