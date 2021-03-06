package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_APPLICATION_SUBMITTED;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

@Controller
public class SubmittedController extends BaseFinalStepController implements StepController {

  private static final String TEMPLATE_APPLICATION_SUBMITTED = "application-end/submitted";

  @Autowired
  SubmittedController(RouteMaster routeMaster, RedirectVersionCookieManager cookieManager) {
    super(routeMaster, cookieManager);
  }

  @Override
  protected String getTemplate() {
    return TEMPLATE_APPLICATION_SUBMITTED;
  }

  @GetMapping(URL_APPLICATION_SUBMITTED)
  public String show(
      SessionStatus sessionStatus,
      HttpServletResponse response,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model) {
    if (null != journey && null != journey.getFormForStep(StepDefinition.CONTACT_DETAILS)) {
      ContactDetailsForm contactForm = journey.getFormForStep(StepDefinition.CONTACT_DETAILS);
      model.addAttribute("contactEmail", contactForm.getEmailAddress());
    }
    return super.show(journey, model, response, sessionStatus);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.SUBMITTED;
  }
}
