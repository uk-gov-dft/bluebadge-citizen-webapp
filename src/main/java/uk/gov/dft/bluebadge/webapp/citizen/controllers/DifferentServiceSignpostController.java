package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_DIFFERENT_SERVICE_SIGNPOST)
public class DifferentServiceSignpostController extends BaseFinalStepController {
  private static final String TEMPLATE = "different-service-signpost";

  @Autowired
  DifferentServiceSignpostController(RouteMaster routeMaster) {
    super(routeMaster);
  }

  @Override
  @GetMapping
  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      SessionStatus sessionStatus) {

    String urlToReturnTo = super.show(journey, model, sessionStatus);
    if (urlToReturnTo.startsWith("redirect")) {
      return urlToReturnTo;
    }

    Optional<LocalAuthorityRefData.LocalAuthorityMetaData> localAuthorityMetadata =
        journey.getLocalAuthority().getLocalAuthorityMetaData();
    Optional<String> differentServiceSignpostUrl =
        localAuthorityMetadata.map(
            LocalAuthorityRefData.LocalAuthorityMetaData::getDifferentServiceSignpostUrl);
    // TODO: What to do if no url, if we are here is because we have the url.
    model.addAttribute(
        "differentServiceSignpostUrl",
        differentServiceSignpostUrl.orElse(
            "https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here"));

    return urlToReturnTo;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.DIFFERENT_SERVICE_SIGNPOST;
  }

  @Override
  protected String getTemplate() {
    return TEMPLATE;
  }
}
