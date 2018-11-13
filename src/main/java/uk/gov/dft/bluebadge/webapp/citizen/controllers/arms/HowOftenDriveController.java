package uk.gov.dft.bluebadge.webapp.citizen.controllers.arms;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.SimpleStepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsHowOftenDriveForm;

@Controller
@RequestMapping(Mappings.URL_ARMS_HOW_OFTEN_DRIVE)
public class HowOftenDriveController extends SimpleStepController {

  @Autowired
  public HowOftenDriveController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @Override
  protected String getTemplate() {
    return "arms/how-often-drive";
  }

  @Override
  protected StepForm getNewFormInstance() {
    return ArmsHowOftenDriveForm.builder().build();
  }

  @GetMapping
  @Override
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    return super.show(journey, model);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ArmsHowOftenDriveForm armsHowOftenDriveForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    return super.submit(journey, armsHowOftenDriveForm, bindingResult, attr);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ARMS_HOW_OFTEN_DRIVE;
  }
}
