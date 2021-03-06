package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.BaseFinalStepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

@Controller
@RequestMapping(Mappings.URL_ORGANISATION_NOT_ELIGIBLE)
public class OrganisationNotEligibleController extends BaseFinalStepController {
  private static final String TEMPLATE = "organisation/organisation-not-eligible";

  @Autowired
  OrganisationNotEligibleController(
      RouteMaster routeMaster, RedirectVersionCookieManager cookieManager) {
    super(routeMaster, cookieManager);
  }

  @Override
  @GetMapping
  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      HttpServletResponse response,
      SessionStatus sessionStatus) {
    return super.show(journey, model, response, sessionStatus);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.ORGANISATION_NOT_ELIGIBLE;
  }

  @Override
  protected String getTemplate() {
    return TEMPLATE;
  }
}
