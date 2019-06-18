package uk.gov.dft.bluebadge.webapp.citizen.controllers.hidden;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.BaseFinalStepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

@Controller
@RequestMapping(Mappings.URL_HIDDEN_DISABILITY)
public class HiddenDisabilityController extends BaseFinalStepController implements StepController {
  private static final String TEMPLATE = "hidden-disability/hidden-disability";

  public HiddenDisabilityController(
      RouteMaster routeMaster, RedirectVersionCookieManager cookieManager) {
    super(routeMaster, cookieManager);
  }

  @Override
  protected String getTemplate() {
    return TEMPLATE;
  }

  @GetMapping
  @Override
  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      HttpServletResponse response,
      SessionStatus sessionStatus) {
    return super.show(journey, model, response, sessionStatus);
  }

  @PostMapping
  public String submit() {
    return "redirect:" + Mappings.URL_APPLICANT_TYPE;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HIDDEN_DISABILITY;
  }
}
