package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Controller
@RequestMapping(Mappings.URL_RECEIVE_BENEFITS)
public class ReceiveBenefitsController implements StepController {

  private static final String TEMPLATE = "receive-benefits";

  private final RouteMaster routeMaster;

  @Autowired
  public ReceiveBenefitsController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(
      @ModelAttribute("formRequest") ReceiveBenefitsForm receiveBenefitsForm,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest") && null != journey.getReceiveBenefitsForm()) {
      model.addAttribute("formRequest", journey.getReceiveBenefitsForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", ReceiveBenefitsForm.builder().build());
    }

    setupModel(model);

    return TEMPLATE;
  }

  private void setupModel(Model model) {
    model.addAttribute("benefitOptions", getBenefitOptions());
  }

  /**
   * This should move to the reference data service. But also need to think about the message code
   * for the descriptions. Surely we need Welsh too?
   *
   * @return
   */
  private RadioOptionsGroup getBenefitOptions() {
    RadioOption pip = new RadioOption(EligibilityCodeField.PIP.name(), "options.benefits.pip");
    RadioOption dla = new RadioOption(EligibilityCodeField.DLA.name(), "options.benefits.dla");
    RadioOption afrfcs =
        new RadioOption(EligibilityCodeField.AFRFCS.name(), "options.benefits.afrfcs");
    RadioOption wpms = new RadioOption(EligibilityCodeField.WPMS.name(), "options.benefits.wpms");
    RadioOption none = new RadioOption(EligibilityCodeField.WALKD.name(), "options.benefits.none");

    List<RadioOption> options = Lists.newArrayList(pip, dla, afrfcs, wpms, none);

    return new RadioOptionsGroup("receiveBenefitsPage.title", options);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") ReceiveBenefitsForm receiveBenefitsForm,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("errorSummary", new ErrorViewModel());
      setupModel(model);
      return TEMPLATE;
    }

    journey.setReceiveBenefitsForm(receiveBenefitsForm);
    return routeMaster.redirectToOnSuccess(receiveBenefitsForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.RECEIVE_BENEFITS;
  }
}
