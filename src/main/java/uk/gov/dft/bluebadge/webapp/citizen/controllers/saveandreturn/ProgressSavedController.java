package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_PROGRESS_SAVED)
public class ProgressSavedController implements StepController {
  static final String TEMPLATE = "save-and-return/progress-saved";
  static final String TIME_TO_EXPIRY_MODEL_KEY = "saveTimeToExpiry";
  static final String TIME_TO_EXPIRY_UNITS_MODEL_KEY = "saveTimeToExpiryUnits";
  static final String UNITS_DAYS = "days";
  static final String UNITS_HOURS = "hours";
  public static final String FORM_REQUEST = "formRequest";
  private RedisService redisService;

  @Autowired
  ProgressSavedController(RedisService redisService) {
    this.redisService = redisService;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      SessionStatus sessionStatus) {

    // If here should have a saved progress form.
    if (null == journey.getSaveProgressForm()
        || StringUtils.isEmpty(journey.getSaveProgressForm().getEmailAddress())) {
      return REDIRECT + Mappings.URL_TASK_LIST;
    }

    model.addAttribute(FORM_REQUEST, journey.getSaveProgressForm());
    String emailAddress = journey.getSaveProgressForm().getEmailAddress();

    long daysToExpiry = redisService.getDaysToExpiryRoundedUp(RedisKeys.JOURNEY, emailAddress);

    if (daysToExpiry <= 1) {
      model.addAttribute(TIME_TO_EXPIRY_UNITS_MODEL_KEY, UNITS_HOURS);
      model.addAttribute(
          TIME_TO_EXPIRY_MODEL_KEY, redisService.getHoursToExpiry(RedisKeys.JOURNEY, emailAddress));
    } else {
      model.addAttribute(TIME_TO_EXPIRY_UNITS_MODEL_KEY, UNITS_DAYS);
      model.addAttribute(TIME_TO_EXPIRY_MODEL_KEY, daysToExpiry);
    }

    sessionStatus.setComplete();
    return TEMPLATE;
  }

  @Override
  public StepDefinition getStepDefinition() {
    // Not a real StepController.  Needs journey
    return null;
  }
}
