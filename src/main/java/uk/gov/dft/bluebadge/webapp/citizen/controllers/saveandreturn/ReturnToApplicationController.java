package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney.SAVE_AND_RETURN_JOURNEY_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.CODE;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.EMAIL_TRIES;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.hashEmailAddress;

import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.MessageService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

@Controller
@Slf4j
@RequestMapping(Mappings.URL_RETURN_TO_APPLICATION)
public class ReturnToApplicationController implements SaveAndReturnController {

  static final String TEMPLATE = "save-and-return/return-to-application";
  public static final String FORM_REQUEST = "formRequest";

  private final CryptoService cryptoService;
  private final RedisService redisService;
  private MessageService messageService;
  private RedirectVersionCookieManager cookieManager;

  private Random random = new Random();

  ReturnToApplicationController(
      CryptoService cryptoService,
      RedisService redisService,
      MessageService messageService,
      RedirectVersionCookieManager cookieManager) {
    this.cryptoService = cryptoService;
    this.redisService = redisService;
    this.messageService = messageService;
    this.cookieManager = cookieManager;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY) SaveAndReturnJourney saveAndReturnJourney) {

    if (!model.containsAttribute(FORM_REQUEST)
        && null != saveAndReturnJourney.getSaveAndReturnForm()) {
      model.addAttribute(FORM_REQUEST, saveAndReturnJourney.getSaveAndReturnForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, SaveAndReturnForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY) SaveAndReturnJourney saveAndReturnJourney,
      @Valid @ModelAttribute(FORM_REQUEST) SaveAndReturnForm saveAndReturnForm,
      BindingResult bindingResult,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return RouteMaster.redirectToOnBindingError(
          Mappings.URL_RETURN_TO_APPLICATION, saveAndReturnForm, bindingResult, attr);
    }

    saveAndReturnJourney.setSaveAndReturnForm(saveAndReturnForm);

    String emailAddress = saveAndReturnForm.getEmailAddress();
    Long postCount = redisService.incrementAndSetExpiryIfNew(EMAIL_TRIES, emailAddress);

    if (journeyExistsInRedis(emailAddress) && throttleNotExceeded(emailAddress, postCount)) {
      send4digitCodeEmail(emailAddress);
      addRedirectCookieIfNecessary(emailAddress, request, response);
      request.getSession().setAttribute(SAVE_AND_RETURN_JOURNEY_KEY, saveAndReturnJourney);
    }

    return REDIRECT + Mappings.URL_ENTER_CODE;
  }

  private void addRedirectCookieIfNecessary(
      String emailAddress, HttpServletRequest request, HttpServletResponse response) {
    // Validate stored session version
    // If version does not match set version cookie for redirect to correct version of
    // application.
    try {
      cryptoService.checkEncryptedJourneyVersion(redisService.get(JOURNEY, emailAddress));
      log.info("Stored journey is the same version as this application instance.");
      cookieManager.addCookie(response);
    } catch (CryptoVersionException e) {
      log.info("Switching citizen app version to {} via cookie.", e.getEncryptedVersion());
      cookieManager.addCookie(response, e);
      // Make sure there is nothing in the session before redirecting to other version.
      // Stops the possibility of incompatible classes
      request.getSession().invalidate();
    }
  }

  private void send4digitCodeEmail(String emailAddress) {
    // Generate and store security code.
    // Use same code for 30 mins and don't regenerate. (Send email repeatedly though).
    String code = getOrCreateSecurityCode(emailAddress);

    log.info(
        "Save and return code:{}, expires: {}",
        code,
        redisService.getExpiryTimeFormatted(CODE, emailAddress));

    // Send email with code.
    messageService.sendReturnToApplicationCodeEmail(
        emailAddress, code, redisService.getExpiryTimeFormatted(CODE, emailAddress));
  }

  private boolean journeyExistsInRedis(String emailAddress) {
    if (redisService.exists(JOURNEY, emailAddress)) {
      return true;
    }
    log.info("No journey exists for email entered. Email hash {}", hashEmailAddress(emailAddress));
    return false;
  }

  private boolean throttleNotExceeded(String emailAddress, Long tries) {
    if (redisService.throttleExceeded(tries)) {
      log.info(
          "Too many tries ({}) at submitting email to resume journey. Email hash {}",
          tries,
          hashEmailAddress(emailAddress));
      return false;
    }
    return true;
  }

  private String getOrCreateSecurityCode(String emailAddress) {
    if (redisService.exists(CODE, emailAddress)) {
      return redisService.get(CODE, emailAddress);
    } else {
      String code = generate4DigitCode();
      redisService.setAndExpire(CODE, emailAddress, code);
      return code;
    }
  }

  String generate4DigitCode() {
    return StringUtils.leftPad(String.valueOf(random.nextInt(9999)), 4, "0");
  }
}
