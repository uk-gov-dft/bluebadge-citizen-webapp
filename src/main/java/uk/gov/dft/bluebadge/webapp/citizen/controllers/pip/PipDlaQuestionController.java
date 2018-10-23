package uk.gov.dft.bluebadge.webapp.citizen.controllers.pip;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.NEVER_RECEIVED_DLA;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm;

@Controller
@RequestMapping(Mappings.URL_PIP_RECEIVED_DLA)
public class PipDlaQuestionController implements StepController {

  private static final String TEMPLATE = "pip/received-dla";

  private final RouteMaster routeMaster;

  @Autowired
  public PipDlaQuestionController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, PipDlaQuestionForm.builder().build());
    }

    model.addAttribute("formOptions", getOptions(journey));

    return TEMPLATE;
  }

  private RadioOptionsGroup getOptions(Journey journey) {
    RadioOption hasReceived =
        new RadioOption(HAS_RECEIVED_DLA, journey.who + "options.pip.has.received");
    RadioOption neverReceived =
        new RadioOption(NEVER_RECEIVED_DLA, journey.who + "options.pip.never.received");

    List<RadioOption> options = Lists.newArrayList(hasReceived, neverReceived);

    String title = journey.who + "pipDlaQuestionPage.content.title";
    return new RadioOptionsGroup(title, options);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PipDlaQuestionForm pipDlaQuestionForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, pipDlaQuestionForm, bindingResult, attr);
    }

    journey.setFormForStep(pipDlaQuestionForm);
    return routeMaster.redirectToOnSuccess(pipDlaQuestionForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PIP_DLA;
  }
}
