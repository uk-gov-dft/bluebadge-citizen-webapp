package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

@Controller
@RequestMapping(Mappings.URL_APPLY_IN_WELSH)
public class ApplyInWelshController {

  private static final String TEMPLATE = "apply-in-welsh";

  @Autowired
  public ApplyInWelshController() {}

  @GetMapping
  public String show() {
    /*
        if (!journey.isValidState(getStepDefinition())) {
          return routeMaster.backToCompletedPrevious();
        }

        if (!model.containsAttribute(FORM_REQUEST) && null != journey.getGenderForm()) {
          model.addAttribute(FORM_REQUEST, journey.getGenderForm());
        }

        if (!model.containsAttribute(FORM_REQUEST)) {
          model.addAttribute(FORM_REQUEST, GenderForm.builder().build());
        }
    */
    //  setupModel(model, journey);

    return TEMPLATE;
  }
  /*
  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) GenderForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setGenderForm(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }*/
  /*
    @Override
    public StepDefinition getStepDefinition() {
      return StepDefinition.GENDER;
    }
  */
  /*
  private void setupModel(Model model, Journey journey) {
    model.addAttribute("options", getOptions(journey));
  }*/

}
