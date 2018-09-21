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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

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
  public String show(
      @ModelAttribute("formRequest") HigherRateMobilityForm formRequest,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute("formRequest") && null != journey.getHigherRateMobilityForm()) {
      model.addAttribute("formRequest", journey.getHigherRateMobilityForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", ReceiveBenefitsForm.builder().build());
    }

    setupModel(model);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") HigherRateMobilityForm formRequest,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("errorSummary", new ErrorViewModel());
      setupModel(model);
      return TEMPLATE;
    }

    journey.setHigherRateMobilityForm(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HIGHER_RATE_MOBILITY;
  }

  private void setupModel(Model model) {
    model.addAttribute("options", getOptions());
  }

  private List<ReferenceData> getOptions() {
    ReferenceData yes = new ReferenceData();
    yes.setShortCode("true");
    yes.setDescription("Yes");

    ReferenceData no = new ReferenceData();
    no.setShortCode("false");
    no.setDescription("No");

    return Lists.newArrayList(yes, no);
  }
}
