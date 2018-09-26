package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;

import javax.validation.Valid;
import java.util.List;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_HIGHER_RATE_MOBILITY)
public class HigherRateMobilityController implements StepController {

  private static final String TEMPLATE = "higher-rate-mobility";

  private final RouteMaster routeMaster;

  @Autowired
  public HigherRateMobilityController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest") && null != journey.getHigherRateMobilityForm()) {
      model.addAttribute("formRequest", journey.getHigherRateMobilityForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", HigherRateMobilityForm.builder().build());
    }

    setupModel(model, journey);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") HigherRateMobilityForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setHigherRateMobilityForm(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HIGHER_RATE_MOBILITY;
  }

  private void setupModel(Model model, Journey journey) {
    model.addAttribute("options", getOptions(journey));
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOption yes = new RadioOption("true", "radio.label.yes");
    RadioOption no = new RadioOption("false", "radio.label.no");

    List<RadioOption> options = Lists.newArrayList(yes, no);

    return new RadioOptionsGroup(journey.who + "higherRateMobilityPage.content.title", options);
  }

  @InitBinder
  public void dataBinding(WebDataBinder binder) {
    binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
  }
}
