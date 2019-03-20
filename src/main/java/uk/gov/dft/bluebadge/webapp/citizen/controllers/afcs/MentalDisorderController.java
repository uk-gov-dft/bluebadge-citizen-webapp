package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.YesNoType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.MentalDisorderForm;

@Controller
@RequestMapping(Mappings.URL_AFCS_MENTAL_DISORDER)
public class MentalDisorderController implements StepController {

  public static final String TEMPLATE = "afcs/mental-disorder";
  private static final String FORM_REQUEST = "formRequest";

  private final RouteMaster routeMaster;

  @Autowired
  MentalDisorderController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, MentalDisorderForm.builder().build());
    }

    RadioOptionsGroup radioOptions =
        new RadioOptionsGroup(journey.getWho() + "afcs.mentalDisorderPage.title")
            .withYesNoOptions(YesNoType.IAM);

    model.addAttribute("radioOptions", radioOptions);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MentalDisorderForm permanentMentalDisorderForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, permanentMentalDisorderForm, bindingResult, attr);
    }

    journey.setFormForStep(permanentMentalDisorderForm);
    return routeMaster.redirectToOnSuccess(permanentMentalDisorderForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.AFCS_MENTAL_DISORDER;
  }
}
