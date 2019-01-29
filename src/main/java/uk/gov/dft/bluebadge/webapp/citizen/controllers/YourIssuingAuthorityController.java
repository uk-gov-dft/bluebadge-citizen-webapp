package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_YOUR_ISSUING_AUTHORITY)
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

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute("formRequest")) {
      // Lookup local authority from council and populate model.
      ChooseYourCouncilForm councilForm = journey.getFormForStep(StepDefinition.CHOOSE_COUNCIL);
      Assert.notNull(
          councilForm, "Got to issuing authority GET, without local council step being completed.");

      YourIssuingAuthorityForm form =
          populateFormFromCouncilCode(councilForm.getCouncilShortCode());
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

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute("formRequest") YourIssuingAuthorityForm yourIssuingAuthorityForm) {

    journey.setFormForStep(yourIssuingAuthorityForm);
    return routeMaster.redirectToOnSuccess(yourIssuingAuthorityForm, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.YOUR_ISSUING_AUTHORITY;
  }
}
