package uk.gov.dft.bluebadge.webapp.citizen.controllers.arms;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.SimpleStepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsAdaptedVehicleForm;

@Controller
@RequestMapping(Mappings.URL_ARMS_DRIVE_ADAPTED_VEHICLE)
public class DriveAdaptedVehicleController extends SimpleStepController {

  @Autowired
  DriveAdaptedVehicleController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @Override
  protected String getTemplate() {
    return "arms/drive-adapted-vehicle";
  }

  @Override
  protected StepForm getNewFormInstance() {
    return ArmsAdaptedVehicleForm.builder().build();
  }

  @GetMapping
  @Override
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    return super.show(journey, model);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ArmsAdaptedVehicleForm armsAdaptedVehicleForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (Boolean.TRUE.equals(armsAdaptedVehicleForm.getHasAdaptedVehicle())
        && StringUtils.isEmpty(armsAdaptedVehicleForm.getAdaptedVehicleDescription())) {
      bindingResult.rejectValue("adaptedVehicleDescription", "NotBlank");
    }

    return super.submit(journey, armsAdaptedVehicleForm, bindingResult, attr);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE;
  }
}
