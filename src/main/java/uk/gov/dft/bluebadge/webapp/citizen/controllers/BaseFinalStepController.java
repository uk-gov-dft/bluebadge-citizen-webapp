package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

// Following to be removed.  Is for unused parameter model in method show.
// Added for build on release day.
@SuppressWarnings("squid:S1172")
public abstract class BaseFinalStepController implements StepController {
  protected final RouteMaster routeMaster;
  protected final RedirectVersionCookieManager cookieManager;

  public BaseFinalStepController(
      RouteMaster routeMaster, RedirectVersionCookieManager cookieManager) {
    this.routeMaster = routeMaster;
    this.cookieManager = cookieManager;
  }

  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      HttpServletResponse response,
      SessionStatus sessionStatus) {
    // This is potentially really bad. As the application could have been submitted and then
    // keeping the session. see BBB-1346
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }
    sessionStatus.setComplete();
    cookieManager.removeCookie(response);
    return getTemplate();
  }

  protected abstract String getTemplate();
}
