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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationCareForm;

@Controller
@RequestMapping(Mappings.URL_ORGANISATION_CARE)
public class OrganisationCareController implements StepController {

  public static final String TEMPLATE = "organisation/organisation-care";

  private final RouteMaster routeMaster;

  @Autowired
  public OrganisationCareController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ORGANISATION_CARE;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      attachForm(model, journey);
    }

    setupModel(model, journey);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) OrganisationCareForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setOrganisationCareForm(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest);
  }

  private void attachForm(Model model, Journey journey) {
    if (null != journey.getOrganisationCareForm()) {
      model.addAttribute(FORM_REQUEST, journey.getOrganisationCareForm());
    } else {
      model.addAttribute(FORM_REQUEST, OrganisationCareForm.builder().build());
    }
  }

  private void setupModel(Model model, Journey journey) {
    RadioOptionsGroup group =
        new RadioOptionsGroup("organisationCare.page.title").autoPopulateBooleanOptions();
    group.setHintKey("organisationCare.page.title.hint");

    model.addAttribute("options", group);
  }
}
