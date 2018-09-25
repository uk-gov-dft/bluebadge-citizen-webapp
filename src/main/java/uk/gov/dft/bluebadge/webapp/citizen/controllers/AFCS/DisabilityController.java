package uk.gov.dft.bluebadge.webapp.citizen.controllers.AFCS;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS.DisabilityForm;

@Controller
@RequestMapping(Mappings.URL_AFCS_DISABILITY)
public class DisabilityController implements StepController {

  public static final String TEMPLATE = "afcs/disability";

  private final RouteMaster routeMaster;
  private static final String FORM_REQUEST = "formRequest";

  @Autowired
  public DisabilityController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getDisabilityForm()) {
      model.addAttribute(FORM_REQUEST, journey.getDisabilityForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, DisabilityForm.builder().build());
    }

    RadioOptionsGroup radioOptions =
        new RadioOptionsGroup("afcs.disabilityPage.title").autoPopulateBooleanOptions();

    model.addAttribute("radioOptions", radioOptions);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) DisabilityForm disabilityForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, disabilityForm, bindingResult, attr);
    }

    journey.setDisabilityForm(disabilityForm);
    return routeMaster.redirectToOnSuccess(disabilityForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.AFCS_DISABILITY;
  }
}
