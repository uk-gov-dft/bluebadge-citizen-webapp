package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_REMOVE_PART;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MOBILITY_AID_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;

@Controller
@RequestMapping(Mappings.URL_MOBILITY_AID_LIST)
public class MobilityAidListController implements StepController {

  private static final String TEMPLATE = "mobility-aid-list";
  private final RouteMaster routeMaster;

  @Autowired
  MobilityAidListController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      // Create object in journey with empty list.
      // Want to not get any null pointers accessing list.
      journey.setFormForStep(MobilityAidListForm.builder().mobilityAids(new ArrayList<>()).build());
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) MobilityAidListForm mobilityAidListForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, mobilityAidListForm, bindingResult, attr);
    }

    MobilityAidListForm journeyListForm = journey.getFormForStep(MOBILITY_AID_LIST);
    // Reset if no selected
    // Treat as No selected if no aids added whilst yes was selected
    if ("no".equals(mobilityAidListForm.getHasWalkingAid())
        || ("yes".equals(mobilityAidListForm.getHasWalkingAid())
            && journeyListForm.getMobilityAids().isEmpty())) {
      journey.setFormForStep(
          MobilityAidListForm.builder()
              .hasWalkingAid("no")
              .mobilityAids(new ArrayList<>())
              .build());
    } else {
      journeyListForm.setHasWalkingAid(mobilityAidListForm.getHasWalkingAid());
      journey.setFormForStep(journeyListForm);
    }

    // Don't overwrite mobility/AidList in journey
    // as it is not bound to inputs in ui form and always null on submit

    return routeMaster.redirectToOnSuccess(mobilityAidListForm, journey);
  }

  @GetMapping(value = URL_REMOVE_PART)
  public String handleDelete(
      @RequestParam(name = "uuid") String uuid,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (journey.hasStepForm(getStepDefinition())
        && null
            != ((MobilityAidListForm) journey.getFormForStep(getStepDefinition()))
                .getMobilityAids()) {
      ((MobilityAidListForm) journey.getFormForStep(getStepDefinition()))
          .getMobilityAids()
          .removeIf(item -> item.getId().equals(uuid));
    }

    return "redirect:" + Mappings.URL_MOBILITY_AID_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return MOBILITY_AID_LIST;
  }
}
