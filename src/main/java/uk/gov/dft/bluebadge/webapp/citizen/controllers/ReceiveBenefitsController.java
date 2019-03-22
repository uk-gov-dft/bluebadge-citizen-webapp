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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

@Controller
@RequestMapping(Mappings.URL_RECEIVE_BENEFITS)
public class ReceiveBenefitsController implements StepController {

  private static final String TEMPLATE = "receive-benefits";

  private final RouteMaster routeMaster;

  @Autowired
  ReceiveBenefitsController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ReceiveBenefitsForm.builder().build());
    }

    model.addAttribute("benefitOptions", getBenefitOptions(journey));

    return TEMPLATE;
  }

  private RadioOptionsGroup getBenefitOptions(Journey journey) {
    RadioOption pip = new RadioOption(EligibilityCodeField.PIP.name(), "options.benefits.pip");
    RadioOption dla = new RadioOption(EligibilityCodeField.DLA.name(), "options.benefits.dla");
    RadioOption afrfcs =
        new RadioOption(EligibilityCodeField.AFRFCS.name(), "options.benefits.afrfcs");
    RadioOption wpms = new RadioOption(EligibilityCodeField.WPMS.name(), "options.benefits.wpms");
    RadioOption none = new RadioOption(EligibilityCodeField.NONE.name(), "options.benefits.none");

    List<RadioOption> options = Lists.newArrayList(pip, dla, afrfcs, wpms, none);

    String title = journey.getWho() + "receiveBenefitsPage.title";
    return new RadioOptionsGroup(title, options);
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ReceiveBenefitsForm receiveBenefitsForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, receiveBenefitsForm, bindingResult, attr);
    }

    journey.setFormForStep(receiveBenefitsForm);
    return routeMaster.redirectToOnSuccess(receiveBenefitsForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.RECEIVE_BENEFITS;
  }
}
