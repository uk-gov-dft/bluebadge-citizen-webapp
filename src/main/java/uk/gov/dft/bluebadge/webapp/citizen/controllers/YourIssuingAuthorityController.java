package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_YOUR_ISSUING_AUTHORITY_CHOOSE_COUNCIL;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.LocaleAwareRefData;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Slf4j
@Controller
@RequestMapping
@SuppressWarnings("{common-java:DuplicatedBlocks}")
public class YourIssuingAuthorityController implements StepController {
  private static final String TEMPLATE = "issuing-authority";

  private final RouteMaster routeMaster;
  private ReferenceDataService referenceDataService;

  @Autowired
  YourIssuingAuthorityController(
      RouteMaster routeMaster, ReferenceDataService referenceDataService) {
    this.routeMaster = routeMaster;
    this.referenceDataService = referenceDataService;
  }

  @GetMapping(Mappings.URL_YOUR_ISSUING_AUTHORITY)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute("formRequest")) {
      // Lookup local authority from council and populate model.
      ChooseYourCouncilForm chooseCouncilForm =
          journey.getFormForStep(StepDefinition.CHOOSE_COUNCIL);
      FindYourCouncilForm findCouncilForm = journey.getFormForStep(StepDefinition.FIND_COUNCIL);

      Assert.isTrue(
          chooseCouncilForm != null || findCouncilForm != null,
          "Got to issuing authority GET, without local council or find local council step being completed.");

      String council = null;
      if ((chooseCouncilForm != null && chooseCouncilForm.getCouncilShortCode() != null)
          || (findCouncilForm != null && journey.getLocalAuthority() != null)) {
        council = journey.getLocalAuthority().getShortCode();
      }
      Assert.notNull(council, "Council must be not null");

      YourIssuingAuthorityForm form = populateFormFromCouncilCode(council);
      if (null != form) {
        model.addAttribute("formRequest", form);
      }
    }

    return TEMPLATE;
  }

  YourIssuingAuthorityForm populateFormFromCouncilCode(String councilCode) {
    LocaleAwareRefData<LocalAuthorityRefData> localAuthorityRefData =
        new LocaleAwareRefData<>(
            referenceDataService.lookupLocalAuthorityFromCouncilCode(councilCode));
    return YourIssuingAuthorityForm.builder()
        .localAuthorityDescription(localAuthorityRefData.getDescription())
        .localAuthorityShortCode(localAuthorityRefData.getShortCode())
        .build();
  }

  @PostMapping(Mappings.URL_YOUR_ISSUING_AUTHORITY)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute("formRequest") YourIssuingAuthorityForm yourIssuingAuthorityForm) {

    journey.setFormForStep(yourIssuingAuthorityForm);
    return routeMaster.redirectToOnSuccess(yourIssuingAuthorityForm, journey);
  }

  @GetMapping(URL_YOUR_ISSUING_AUTHORITY_CHOOSE_COUNCIL)
  public String redirectToChooseCouncil(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    if (journey != null) {
      FindYourCouncilForm findYourCouncilForm = journey.getFormForStep(StepDefinition.FIND_COUNCIL);
      if (findYourCouncilForm != null) {
        findYourCouncilForm.setPostcode("");
      }
      ChooseYourCouncilForm chooseYourCouncilForm =
          journey.getFormForStep(StepDefinition.CHOOSE_COUNCIL);
      if (chooseYourCouncilForm != null) {
        chooseYourCouncilForm.setCouncilShortCode("");
      }
    }
    return "redirect:" + Mappings.URL_CHOOSE_YOUR_COUNCIL;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.YOUR_ISSUING_AUTHORITY;
  }
}
