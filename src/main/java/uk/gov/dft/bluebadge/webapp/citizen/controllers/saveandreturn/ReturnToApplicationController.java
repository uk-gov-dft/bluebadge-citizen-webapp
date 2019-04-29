package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn.EnterCodeController.getVersionCookie;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn.SaveAndReturnController.SAVE_AND_RETURN_JOURNEY_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@Slf4j
@RequestMapping(Mappings.URL_RETURN_TO_APPLICATION)
public class ReturnToApplicationController implements SaveAndReturnController {

  private static final String TEMPLATE = "save-and-return/return-to-application";
  public static final String FORM_REQUEST = "formRequest";
  private final CryptoService cryptoService;
  private final RedisService redisService;


  public ReturnToApplicationController(CryptoService cryptoService, RedisService redisService) {
    this.cryptoService = cryptoService;
    this.redisService = redisService;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY) SaveAndReturnJourney saveAndReturnJourney) {

    if(null == saveAndReturnJourney.getSaveAndReturnForm()){
      saveAndReturnJourney.setSaveAndReturnForm(SaveAndReturnForm.builder().build());
    }

    if(!model.containsAttribute(FORM_REQUEST)){
      model.addAttribute(FORM_REQUEST, saveAndReturnJourney.getSaveAndReturnForm());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute("saveAndReturnJourney") SaveAndReturnJourney journey,
      @Valid @ModelAttribute(FORM_REQUEST) SaveAndReturnForm saveAndReturnForm,
      HttpServletRequest request,
      HttpServletResponse response,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return redirectToOnBindingError(Mappings.URL_RETURN_TO_APPLICATION, saveAndReturnForm, bindingResult, attr);
    }

    // If email address matches a saved journey then send email and security code.
    // Else just redirect.
    if (redisService.journeyExistsForEmail(
        saveAndReturnForm.getEmailAddress())) {
      // Generate and store security code.
      // Use same code for 30 mins and don't regenerate. (Send email repeatedly though).
      String code;
      if(redisService.securityCodeExistsForEmail(saveAndReturnForm.getEmailAddress())){
        code = redisService.getCodeOnReturn(saveAndReturnForm.getEmailAddress());
      }else {
        code = redisService.setCodeForReturn(
            saveAndReturnForm.getEmailAddress());
      }

      // TODO
      // Send email with code.
      // TODO remove me!!!!! (Without email address context fairly useless though).
      log.info("Save and return code {}", code);

      // Retrieve the stored session
      // Validate it's version
      // If version does not match set version cookie.
      try {
        cryptoService.decryptJourney(
            redisService.getEncryptedJourneyOnReturn(
                saveAndReturnJourney().getSaveAndReturnForm().getEmailAddress()),
            "1.0.0");
      } catch (CryptoVersionException e) {
        response.addCookie(getVersionCookie(e.getEncryptedVersion()));
      }
    }

    return REDIRECT + Mappings.URL_ENTER_CODE;
  }
}
