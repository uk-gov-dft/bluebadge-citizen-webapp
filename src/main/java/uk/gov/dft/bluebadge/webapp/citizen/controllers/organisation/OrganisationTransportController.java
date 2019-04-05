package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.YesNoType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationTransportForm;

@Controller
@RequestMapping(Mappings.URL_ORGANISATION_TRANSPORT)
public class OrganisationTransportController implements StepController {

  public static final String TEMPLATE = "organisation/organisation-transport";

  private final RouteMaster routeMaster;

  @Autowired
  OrganisationTransportController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ORGANISATION_TRANSPORT;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      attachForm(model, journey);
    }

    setupModel(model);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) OrganisationTransportForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setFormForStep(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private void attachForm(Model model, Journey journey) {
    if (journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    } else {
      model.addAttribute(FORM_REQUEST, OrganisationTransportForm.builder().build());
    }
  }

  private void setupModel(Model model) {
    model.addAttribute(
        "options",
        new RadioOptionsGroup("organisationTransport.page.title").withYesNoOptions(YesNoType.ITIS));
  }
}
