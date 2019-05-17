package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.NotFoundException;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
@Slf4j
public class FindYourCouncilController implements StepController {

  private static final String TEMPLATE = "find-council";
  private static final String FORM_REQUEST = "formRequest";

  private ReferenceDataService referenceDataService;
  private final RouteMaster routeMaster;

  @Autowired
  FindYourCouncilController(ReferenceDataService referenceDataService, RouteMaster routeMaster) {
    this.referenceDataService = referenceDataService;
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_FIND_YOUR_COUNCIL)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, FindYourCouncilForm.builder().build());
    }

    return TEMPLATE;
  }

  @GetMapping(Mappings.URL_FIND_YOUR_COUNCIL_BYPASS)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    FindYourCouncilForm formRequest = FindYourCouncilForm.builder().build();
    if (journey != null) {
      journey.setFormForStep(formRequest);
    }
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @PostMapping(Mappings.URL_FIND_YOUR_COUNCIL)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) FindYourCouncilForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    try {
      LocalAuthorityRefData localAuthority =
          referenceDataService.retrieveLAByPostcode(formRequest.getPostcode());
      journey.setLocalAuthority(localAuthority);
      journey.setFormForStep(formRequest);
      // We populate this form because we skip it but still want to keep the linearility of state
      // transitions to make implementation easier.
      ChooseYourCouncilForm chooseYourCouncilFormRequest =
          ChooseYourCouncilForm.builder().councilShortCode(localAuthority.getShortCode()).build();
      journey.setFormForStep(chooseYourCouncilFormRequest);
    } catch (NotFoundException nfex) {
      bindingResult.rejectValue("postcode", "NotFound.findYourcouncil.postcode");
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    } catch (Exception ex) {
      log.error(
          "Unexpected exception looking up council for postcode:" + formRequest.getPostcode(), ex);
      bindingResult.rejectValue("postcode", "ServerError.findYourcouncil.postcode");
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.FIND_COUNCIL;
  }
}
