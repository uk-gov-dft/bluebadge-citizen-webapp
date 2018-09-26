package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PIP.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

import javax.validation.Valid;
import java.util.List;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PIP.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_12;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.CHILDB;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.CHILDVEH;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.TERMILL;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm.MainReasonOption.WALKD;

@Controller
@RequestMapping(Mappings.URL_MAIN_REASON)
public class MainReasonController implements StepController {
  private static final String TEMPLATE = "mainreason/main-reason";

  private final RouteMaster routeMaster;

  @Autowired
  public MainReasonController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getMainReasonForm()) {
      model.addAttribute(FORM_REQUEST, journey.getMainReasonForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, MainReasonForm.builder().build());
    }

    model.addAttribute("formOptions",
        new RadioOptionsGroup.Builder().titleMessageKeyApplicantAware("mainReasonPage.content.title", journey)
    .addOptionApplicantAware(TERMILL, "options.mainReasonPage.termill", journey)
            .addOptionApplicantAware(BLIND,"options.mainReasonPage.blind", journey)
            .addOptionApplicantAware(WALKD,"options.mainReasonPage.walkd", journey)
            .addOptionApplicantAware(ARMS,"options.mainReasonPage.arms", journey)
            .addOptionApplicantAware(CHILDB,"options.mainReasonPage.childb", journey)
            .addOptionApplicantAware(CHILDVEH,"options.mainReasonPage.childveh", journey)
            .addOptionApplicantAware(NONE,"options.mainReasonPage.none", journey)
    .build());

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MainReasonForm mainReasonForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, mainReasonForm, bindingResult, attr);
    }

    journey.setMainReasonForm(mainReasonForm);

    return routeMaster.redirectToOnSuccess(mainReasonForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MAIN_REASON;
  }
}
