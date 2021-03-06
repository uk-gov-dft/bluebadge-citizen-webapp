package uk.gov.dft.bluebadge.webapp.citizen.controllers.pip;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_0;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_10;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_12;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_4;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_8;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm;

@Controller
@RequestMapping(Mappings.URL_PIP_PLANNING_JOURNEY)
public class PipPlanningJourneyController implements StepController {

  private static final String TEMPLATE = "pip/planning-journey";

  private final RouteMaster routeMaster;

  @Autowired
  PipPlanningJourneyController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, PipPlanningJourneyForm.builder().build());
    }

    model.addAttribute("formOptions", getOptions(journey));
    return TEMPLATE;
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOption points12 = new RadioOption(PLANNING_POINTS_12, "options.pip.planning.points12");
    RadioOption points10 = new RadioOption(PLANNING_POINTS_10, "options.pip.planning.points10");
    RadioOption points8 = new RadioOption(PLANNING_POINTS_8, "options.pip.planning.points8");
    RadioOption points4 = new RadioOption(PLANNING_POINTS_4, "options.pip.planning.points4");
    RadioOption points0 = new RadioOption(PLANNING_POINTS_0, "options.pip.planning.points0");

    List<RadioOption> options = Lists.newArrayList(points12, points10, points8, points4, points0);

    String title = journey.getWho() + "pipPlanningAndFollowingPage.content.title";
    return new RadioOptionsGroup(title, options);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PipPlanningJourneyForm pipPlanningJourneyForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(
          this, pipPlanningJourneyForm, bindingResult, attr);
    }

    journey.setFormForStep(pipPlanningJourneyForm);

    return routeMaster.redirectToOnSuccess(pipPlanningJourneyForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PIP_PLANNING_JOURNEY;
  }
}
