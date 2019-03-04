package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationSubmitForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

@Controller
@RequestMapping(Mappings.URL_DECLARATIONS)
public class DeclarationSubmitController implements StepController {

  private static final String TEMPLATE = "application-end/declaration";

  private final ApplicationManagementService appService;
  private final RouteMaster routeMaster;

  @Autowired
  DeclarationSubmitController(ApplicationManagementService appService, RouteMaster routeMaster) {
    this.appService = appService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", DeclarationSubmitForm.builder().build());
    }
    model.addAttribute(
        "submitButtonMessage",
        journey.isPaymentsEnabled()
            ? "declarationPage.continue.button.label"
            : "declarationPage.submit.button.label");

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") DeclarationSubmitForm form,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, form, bindingResult, attr);
    }
    if (!journey.isPaymentsEnabled()) {
      appService.create(JourneyToApplicationConverter.convert(journey));
    }
    journey.setFormForStep(form);
    return routeMaster.redirectToOnSuccess(form, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.DECLARATIONS;
  }
}
