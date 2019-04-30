package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.EnterCodeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoPostcodeException;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_ENTER_CODE)
public class EnterCodeController implements SaveAndReturnController, StepController {
  private static final String TEMPLATE = "save-and-return/enter-code";
  public static final String FORM_REQUEST = "formRequest";
  private CryptoService cryptoService;
  private RedisService redisService;

  @Autowired
  public EnterCodeController(CryptoService cryptoService, RedisService redisService) {
    this.cryptoService = cryptoService;
    this.redisService = redisService;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY) SaveAndReturnJourney saveAndReturnJourney) {

    if (null == saveAndReturnJourney.getEnterCodeForm()) {
      saveAndReturnJourney.setEnterCodeForm(new EnterCodeForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, saveAndReturnJourney.getEnterCodeForm());
    }

    // emailAddress needed in ui
    model.addAttribute(
        "emailAddress", saveAndReturnJourney.getSaveAndReturnForm().getEmailAddress());

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY) SaveAndReturnJourney journey,
      @Valid @ModelAttribute(FORM_REQUEST) EnterCodeForm enterCodeForm,
      HttpServletRequest request,
      HttpServletResponse response,
      BindingResult bindingResult,
      RedirectAttributes attr)
      throws CryptoVersionException {

    if (bindingResult.hasErrors()) {
      return redirectToOnBindingError(Mappings.URL_ENTER_CODE, enterCodeForm, bindingResult, attr);
    }

    String emailAddress = journey.getSaveAndReturnForm().getEmailAddress();
    boolean loadJourney =
        redisService.journeyExistsForEmail(emailAddress);
    Long postCount = redisService.incrementCodePostCount(emailAddress);
    if (loadJourney && !redisService.emailCodeLimitExceeded(postCount)) {
      // Check code.
      String storedCode =
          redisService.getCodeOnReturn(emailAddress);
      if (StringUtils.isEmpty(storedCode)) {
        loadJourney = false;
      } else {
        // Compare stored code to entered code
        loadJourney = storedCode.equalsIgnoreCase(enterCodeForm.getCode().trim());
      }
    }

    // Need the journey loaded from here...
    Journey storedJourney = null;

    if (loadJourney) {
      // Check postcode
      try {
        storedJourney =
            cryptoService.decryptJourney(
                redisService.getEncryptedJourneyOnReturn(
                    emailAddress),
                "1.0.0",
                saveAndReturnJourney().getEnterCodeForm().getPostcode());
      } catch (CryptoPostcodeException e) {
        loadJourney = false;
      }
    }

    if (loadJourney) {
      request.getSession().setAttribute(JOURNEY_SESSION_KEY, storedJourney);
      return REDIRECT + Mappings.URL_TASK_LIST;
    }else{
      bindingResult.reject("enter.code.no.applications");
      return redirectToOnBindingError(Mappings.URL_ENTER_CODE, enterCodeForm, bindingResult, attr);
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return null;
  }

  /**
   * A cookie to set to allow nginx to redirect to correct version of citizen webapp.
   *
   * @param version e.g. 1.0.1
   * @return The cookie.
   */
  public static Cookie getVersionCookie(String version) {
    Cookie cookie = new Cookie("BlueBadgeAppVersion", version);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setDomain("uk-gov-dft.gov.uk");
    return cookie;
  }
}
