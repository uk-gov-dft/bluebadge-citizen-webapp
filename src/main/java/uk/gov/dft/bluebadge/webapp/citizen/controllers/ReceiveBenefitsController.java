package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
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

    if (!journey.isValidState(receiveBenefitsForm.getAssociatedStep())) {
      return routeMaster.backToCompletedPrevious();
    }

    setupModel(model);

    if (null != journey.getReceiveBenefitsForm()) {
      BeanUtils.copyProperties(journey.getReceiveBenefitsForm(), receiveBenefitsForm);
    }

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
  private List<ReferenceData> getBenefitOptions() {
    ReferenceData pip = new ReferenceData();
    pip.setShortCode(EligibilityCodeField.PIP.name());
    pip.setDescription("Personal Independence Payment (PIP)");

    ReferenceData dla = new ReferenceData();
    dla.setShortCode(EligibilityCodeField.DLA.name());
    dla.setDescription("Disability Living Allowance (DLA)");

    ReferenceData afrfcs = new ReferenceData();
    afrfcs.setShortCode(EligibilityCodeField.AFRFCS.name());
    afrfcs.setDescription("Armed Forces Compensation scheme");

    ReferenceData wpms = new ReferenceData();
    wpms.setShortCode(EligibilityCodeField.WPMS.name());
    wpms.setDescription("War Pensioners' Mobility Supplement");

    ReferenceData none = new ReferenceData();
    none.setShortCode(EligibilityCodeField.WALKD.name());
    none.setDescription("None of these benefits");

    return Lists.newArrayList(pip, dla, afrfcs, wpms, none);
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
