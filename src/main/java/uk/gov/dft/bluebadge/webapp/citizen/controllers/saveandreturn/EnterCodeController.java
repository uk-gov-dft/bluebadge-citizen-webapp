package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.CODE;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.CODE_TRIES;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.EnterCodeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoPostcodeException;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_ENTER_CODE)
public class EnterCodeController implements SaveAndReturnController {
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
      BindingResult bindingResult,
      HttpServletRequest request,
      RedirectAttributes attr,
      SessionStatus sessionStatus)
      throws CryptoVersionException {

    if (bindingResult.hasErrors()) {
      return redirectToOnBindingError(Mappings.URL_ENTER_CODE, enterCodeForm, bindingResult, attr);
    }

    String emailAddress = journey.getSaveAndReturnForm().getEmailAddress();
    Long postCount = redisService.incrementAndSetExpiryIfNew(CODE_TRIES, emailAddress);

    if (journeyExists(emailAddress)
        && throttleNotExceeded(postCount)
        && codesMatch(emailAddress, enterCodeForm.getCode())) {
      try {
        Journey storedJourney =
            cryptoService.decryptJourney(
                redisService.get(JOURNEY, emailAddress), enterCodeForm.getPostcode());
        sessionStatus.setComplete();
        request.getSession().setAttribute(JOURNEY_SESSION_KEY, storedJourney);
        return REDIRECT + Mappings.URL_TASK_LIST;
      } catch (CryptoPostcodeException e) {
        log.info(
            "Attempt to return to application with postcode mismatch {} vs {}.",
            e.getEnteredPostcode(),
            e.getSavedPostcode());
      }
    }

    // Reject - could not load/find saved session
    bindingResult.reject("enter.code.no.applications");
    return redirectToOnBindingError(Mappings.URL_ENTER_CODE, enterCodeForm, bindingResult, attr);
  }

  boolean journeyExists(String emailAddress) {
    if (!redisService.exists(JOURNEY, emailAddress)) {
      log.info("Attempt to return to application when no saved app existed for email address");
      return false;
    }
    return true;
  }

  boolean throttleNotExceeded(Long postCount) {
    if (redisService.throttleExceeded(postCount)) {
      log.info("Too many tries posting 4-digit code to retrieve application.");
      return false;
    }
    return true;
  }

  boolean codesMatch(String emailAddress, String enteredCode) {
    String storedCode = redisService.get(CODE, emailAddress);
    if (StringUtils.isEmpty(storedCode)) {
      log.info(
          "Attempt to return to application when no 4-digit code stored in redis for email address.");
      return false;
    }
    if (!storedCode.equalsIgnoreCase(enteredCode.trim())) {
      log.info("Attempt to return to application when stored code did not match entered code.");
      return false;
    }
    return true;
  }
}
