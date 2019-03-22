package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_DIFFERENT_SERVICE_SIGNPOST_CHOOSE_COUNCIL;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_DIFFERENT_SERVICE_SIGNPOST_CONTINUE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;

@Slf4j
@Controller
@SuppressWarnings({"common-java:DuplicatedBlocks"})
public class DifferentServiceSignpostController implements StepController {
  private static final String TEMPLATE = "different-service-signpost";

  private String defaultDifferentServiceSignpostUrl;
  private final RouteMaster routeMaster;

  @Autowired
  DifferentServiceSignpostController(
      RouteMaster routeMaster,
      @Value("blue-badge.defaultDifferentServiceSignpostUrl")
          String defaultDifferentServiceSignpostUrl) {
    this.routeMaster = routeMaster;
    this.defaultDifferentServiceSignpostUrl = defaultDifferentServiceSignpostUrl;
  }

  @GetMapping(Mappings.URL_DIFFERENT_SERVICE_SIGNPOST)
  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      SessionStatus sessionStatus) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    return TEMPLATE;
  }

  @GetMapping(URL_DIFFERENT_SERVICE_SIGNPOST_CONTINUE)
  public String redirectToThirdParty(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, SessionStatus sessionStatus) {
    String url = getDifferentServiceSignpostUrl(journey);
    if (sessionStatus != null) {
      sessionStatus.setComplete();
    }
    return "redirect:" + url;
  }

  @GetMapping(URL_DIFFERENT_SERVICE_SIGNPOST_CHOOSE_COUNCIL)
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
    return StepDefinition.DIFFERENT_SERVICE_SIGNPOST;
  }

  private String getDifferentServiceSignpostUrl(Journey journey) {

    Optional<LocalAuthorityRefData.LocalAuthorityMetaData> localAuthorityMetadata =
        (journey != null && journey.getLocalAuthority() != null)
            ? journey.getLocalAuthority().getLocalAuthorityMetaData()
            : Optional.empty();
    Optional<String> differentServiceSignpostUrl =
        localAuthorityMetadata.map(
            LocalAuthorityRefData.LocalAuthorityMetaData::getDifferentServiceSignpostUrl);
    return differentServiceSignpostUrl.orElse(defaultDifferentServiceSignpostUrl);
  }
}
