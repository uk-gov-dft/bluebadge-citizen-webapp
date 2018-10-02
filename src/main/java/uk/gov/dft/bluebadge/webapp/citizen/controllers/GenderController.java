package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;

@Controller
@RequestMapping(Mappings.URL_GENDER)
public class GenderController implements StepController {

  private static final String TEMPLATE = "gender";

  private final RouteMaster routeMaster;

  @Autowired
  public GenderController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getGenderForm()) {
      model.addAttribute(FORM_REQUEST, journey.getGenderForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, GenderForm.builder().build());
    }

    setupModel(model, journey);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) GenderForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setGenderForm(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.GENDER;
  }

  private void setupModel(Model model, Journey journey) {
    model.addAttribute("options", getOptions(journey));
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOption male =
        new RadioOption(GenderCodeField.MALE.name(), journey.ageGroup + "radio.label.male");
    RadioOption female =
        new RadioOption(GenderCodeField.FEMALE.name(), journey.ageGroup + "radio.label.female");
    RadioOption unspecified =
        new RadioOption(
            GenderCodeField.UNSPECIFIE.name(), journey.who + "." + journey.ageGroup + "radio.label.unspecified");

    List<RadioOption> options = Lists.newArrayList(male, female, unspecified);

    return new RadioOptionsGroup(journey.who + "genderPage.body.title", options);
  }
}
