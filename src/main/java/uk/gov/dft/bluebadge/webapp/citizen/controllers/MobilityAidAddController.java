package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MOBILITY_AID_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;

@Controller
@RequestMapping(Mappings.URL_MOBILITY_AID_ADD)
public class MobilityAidAddController implements StepController {

  private RouteMaster routeMaster;
  private static final String TEMPLATE = "mobility-aid-add";

  @Autowired
  MobilityAidAddController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, new MobilityAidAddForm());
    }

    model.addAttribute(
        "howProvidedOptions",
        new RadioOptionsGroup.Builder()
            .titleIsLabel()
            .titleMessageKey("mobilityaid.howprovided.optiongroup.title")
            .addOption(HowProvidedCodeField.PERSON, "mobilityaid.howprovided.option.person")
            .addOption(HowProvidedCodeField.PRESCRIBE, "mobilityaid.howprovided.option.prescribed")
            .addOption(HowProvidedCodeField.PRIVATE, "mobilityaid.howprovided.option.private")
            .addOption(HowProvidedCodeField.SOCIAL, "mobilityaid.howprovided.option.social")
            .build());
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MobilityAidAddForm mobilityAidAddForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, mobilityAidAddForm, bindingResult, attr);
    }

    MobilityAidListForm listForm = journey.getFormForStep(MOBILITY_AID_LIST);
    if (null == listForm) {
      listForm = MobilityAidListForm.builder().build();
    }

    listForm.setHasWalkingAid("yes");
    listForm.addMobilityAid(mobilityAidAddForm);
    journey.setFormForStep(listForm);

    return "redirect:" + Mappings.URL_MOBILITY_AID_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MOBILITY_AID_ADD;
  }
}
