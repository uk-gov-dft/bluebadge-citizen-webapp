package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.DANGEROUS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.HELP;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PAIN;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PLAN;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;

@Controller
@RequestMapping(Mappings.URL_WALKING_DIFFICULTY)
public class WalkingDifficultyController implements StepController {
  private static final String TEMPLATE = "mainreason/walking-difficulty";

  private final RouteMaster routeMaster;

  @Autowired
  public WalkingDifficultyController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getWalkingDifficultyForm()) {
      model.addAttribute(FORM_REQUEST, journey.getWalkingDifficultyForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, WalkingDifficultyForm.builder().build());
    }

    model.addAttribute("formOptions", getOptions(journey));

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) WalkingDifficultyForm walkingDifficultyForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, walkingDifficultyForm, bindingResult, attr);
    }

    journey.setWalkingDifficultyForm(walkingDifficultyForm);

    return routeMaster.redirectToOnSuccess(walkingDifficultyForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.WALKING_DIFFICULTY;
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOptionsGroup.Builder optionsBuilder =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("walkingDifficultyPage.content.title", journey)
            .addOptionApplicantAware(HELP, "options.walkingDifficultyPage.help", journey);

    if (journey.getNation().equals(Nation.SCO) || journey.getNation().equals(Nation.WLS)) {
      optionsBuilder.addOptionApplicantAware(PLAN, "options.walkingDifficultyPage.plan", journey);
    }

    optionsBuilder
        .addOptionApplicantAware(PAIN, "options.walkingDifficultyPage.pain", journey)
        .addOptionApplicantAware(DANGEROUS, "options.walkingDifficultyPage.dangerous", journey)
        .addOptionApplicantAware(NONE, "options.walkingDifficultyPage.none", journey);
    return optionsBuilder.build();
  }
}
