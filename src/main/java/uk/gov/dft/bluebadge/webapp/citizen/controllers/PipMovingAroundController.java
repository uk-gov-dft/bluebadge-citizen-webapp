package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm;

import javax.validation.Valid;
import java.util.List;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_0;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_10;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_12;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_4;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_8;

@Controller
@RequestMapping(Mappings.URL_PIP_MOVING_AROUND)
public class PipMovingAroundController implements StepController {

  private static final String TEMPLATE = "pip-moving-around";

  private final RouteMaster routeMaster;

  @Autowired
  public PipMovingAroundController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest") && null != journey.getPipMovingAroundForm()) {
      model.addAttribute("formRequest", journey.getPipMovingAroundForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", PipMovingAroundForm.builder().build());
    }

    model.addAttribute("formOptions", getOptions(journey));

    return TEMPLATE;
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOption points12 = new RadioOption(MOVING_POINTS_12, "options.pip.moving.points12");
    RadioOption points10 = new RadioOption(MOVING_POINTS_10, "options.pip.moving.points10");
    RadioOption points8 = new RadioOption(MOVING_POINTS_8, "options.pip.moving.points8");
    RadioOption points4 = new RadioOption(MOVING_POINTS_4, "options.pip.moving.points4");
    RadioOption points0 = new RadioOption(MOVING_POINTS_0, "options.pip.moving.points0");

    List<RadioOption> options = Lists.newArrayList(points12, points10, points8, points4, points0);

    String title = journey.applicantContextContent("pipMovingAroundPage.content.title");
    return new RadioOptionsGroup(title, options);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") PipMovingAroundForm pipMovingAroundForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, pipMovingAroundForm, bindingResult, attr);
    }

    journey.setPipMovingAroundForm(pipMovingAroundForm);

    return routeMaster.redirectToOnSuccess(pipMovingAroundForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

}
