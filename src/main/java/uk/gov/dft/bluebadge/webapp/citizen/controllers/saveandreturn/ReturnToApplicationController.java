package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.CODE;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.EMAIL_TRIES;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

import java.util.Random;
import javax.servlet.http.Cookie;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.MessageService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

@Controller
@Slf4j
@RequestMapping(Mappings.URL_RETURN_TO_APPLICATION)
public class ReturnToApplicationController implements SaveAndReturnController {

  private static final String TEMPLATE = "save-and-return/return-to-application";
  public static final String FORM_REQUEST = "formRequest";
  private final CryptoService cryptoService;
  private final RedisService redisService;
  private MessageService messageService;
  private Random random = new Random();

  public ReturnToApplicationController(
      CryptoService cryptoService, RedisService redisService, MessageService messageService) {
    this.cryptoService = cryptoService;
    this.redisService = redisService;
    this.messageService = messageService;
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
      HttpServletResponse response,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return redirectToOnBindingError(
          Mappings.URL_RETURN_TO_APPLICATION, saveAndReturnForm, bindingResult, attr);
    }

    saveAndReturnJourney.setSaveAndReturnForm(saveAndReturnForm);

    String emailAddress = saveAndReturnForm.getEmailAddress();
    Long postCount = redisService.incrementAndSetExpiryIfNew(EMAIL_TRIES, emailAddress);
    // If email address matches a saved journey then send email and security code.
    // Don't do anything if throttle exceeded.
    // Else just redirect.
    if (redisService.exists(JOURNEY, emailAddress) && !redisService.throttleExceeded(postCount)) {

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

      // Validate stored session version
      // If version does not match set version cookie.
      try {
        cryptoService.checkEncryptedJourneyVersion(redisService.get(JOURNEY, emailAddress));
      } catch (CryptoVersionException e) {
        log.info("Switching citizen app version to {} via cookie.", e.getEncryptedVersion());
        response.addCookie(getVersionCookie(e.getEncryptedVersion()));
      }
    }

    return REDIRECT + Mappings.URL_ENTER_CODE;
  }

  /**
   * A cookie to set to allow nginx to redirect to correct version of citizen webapp.
   *
   * @param version e.g. 1.0.1
   * @return The cookie.
   */
  Cookie getVersionCookie(String version) {
    Cookie cookie = new Cookie("BlueBadgeAppVersion", version);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    // Default to domain creating cookie (us).
    // cookie.setDomain("uk-gov-dft.gov.uk");
    return cookie;
  }

  String getOrCreateSecurityCode(String emailAddress) {
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
