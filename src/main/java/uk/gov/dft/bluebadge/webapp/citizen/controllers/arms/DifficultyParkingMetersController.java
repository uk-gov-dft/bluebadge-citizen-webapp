package uk.gov.dft.bluebadge.webapp.citizen.controllers.arms;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsDifficultyParkingMetersForm;

import javax.validation.Valid;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_ARMS_DIFFICULTY_PARKING_METERS)
public class DifficultyParkingMetersController extends SimpleStepController {

  @Autowired
  public DifficultyParkingMetersController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @Override
  protected String getTemplate() {
    return "arms/difficulty-parking-meters";
  }

  @Override
  protected StepForm getNewFormInstance() {
    return ArmsDifficultyParkingMetersForm.builder().build();
  }

  @GetMapping
  @Override
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    return super.show(journey, model);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST)
          ArmsDifficultyParkingMetersForm armsDifficultyParkingMetersForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    return super.submit(journey, armsDifficultyParkingMetersForm, bindingResult, attr);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ARMS_DIFFICULTY_PARKING_METER;
  }
}
