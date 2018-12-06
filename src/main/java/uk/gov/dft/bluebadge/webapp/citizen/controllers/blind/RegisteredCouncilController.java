package uk.gov.dft.bluebadge.webapp.citizen.controllers.blind;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
@RequestMapping(Mappings.URL_REGISTERED_COUNCIL)
public class RegisteredCouncilController implements StepController {

  public static final String TEMPLATE = "blind/registered-council";
  public static final String FORM_REQUEST = "formRequest";

  private final ReferenceDataService referenceDataService;
  private final RouteMaster routeMaster;

  @Autowired
  public RegisteredCouncilController(
      ReferenceDataService referenceDataService, RouteMaster routeMaster) {
    this.referenceDataService = referenceDataService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, RegisteredCouncilForm.builder().build());
    }

    RadioOptionsGroup radioOptions =
        new RadioOptionsGroup(journey.who + "registeredCouncilPage.title").withYesNoOptions();
    model.addAttribute("radioOptions", radioOptions);

    List<ReferenceData> councils =
        referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    model.addAttribute("councils", councils);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) RegisteredCouncilForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    LocalAuthorityRefData localAuthority =
        referenceDataService.lookupLocalAuthorityFromCouncilCode(
            formRequest.getRegisteredCouncil());
    formRequest.setLocalAuthorityForRegisteredBlind(localAuthority);
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.REGISTERED_COUNCIL;
  }
}
