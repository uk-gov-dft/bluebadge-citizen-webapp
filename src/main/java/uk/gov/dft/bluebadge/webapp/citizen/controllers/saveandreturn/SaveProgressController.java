package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveProgressForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.MessageService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_SAVE_PROGRESS)
public class SaveProgressController implements StepController {
  static final String TEMPLATE = "save-and-return/save-progress";
  static final String HIDE_POSTCODE_MODEL_KEY = "hidePostcode";
  static final String FORM_REQUEST = "formRequest";

  private RedisService redisService;
  private CryptoService cryptoService;
  private MessageService messageService;

  @Autowired
  SaveProgressController(
      RedisService redisService, CryptoService cryptoService, MessageService messageService) {
    this.redisService = redisService;
    this.cryptoService = cryptoService;
    this.messageService = messageService;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    StepForm form = journey.getFormForStep(StepDefinition.getFirstStep());
    if (null == form) {
      return REDIRECT + Mappings.getUrl(StepDefinition.HOME);
    }

    ContactDetailsForm contactDetailsForm = journey.getFormForStep(StepDefinition.CONTACT_DETAILS);
    EnterAddressForm enterAddressForm = journey.getFormForStep(StepDefinition.ADDRESS);

    if (null != contactDetailsForm && null != contactDetailsForm.getEmailAddress()) {
      model.addAttribute(HIDE_POSTCODE_MODEL_KEY, true);
    } else {
      model.addAttribute(HIDE_POSTCODE_MODEL_KEY, false);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {

      String postcode = null;
      String emailAddress = null;
      if (null != enterAddressForm && null != enterAddressForm.getPostcode()) {
        postcode = enterAddressForm.getPostcode();
      }
      if (null != contactDetailsForm && null != contactDetailsForm.getEmailAddress()) {
        emailAddress = contactDetailsForm.getEmailAddress();
      }
      model.addAttribute(
          FORM_REQUEST,
          SaveProgressForm.builder().emailAddress(emailAddress).postcode(postcode).build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) SaveProgressForm saveProgressForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    StepForm form = journey.getFormForStep(StepDefinition.getFirstStep());
    if (null == form) {
      return REDIRECT + Mappings.getUrl(StepDefinition.HOME);
    }

    if (bindingResult.hasErrors()) {
      return RouteMaster.redirectToOnBindingError(
          Mappings.URL_SAVE_PROGRESS, saveProgressForm, bindingResult, attr);
    }

    journey.setSaveProgressForm(saveProgressForm);

    log.debug("Saving user journey.");
    String cipher = cryptoService.encryptJourney(journey, saveProgressForm.getPostcode());
    redisService.setAndExpireIfNew(JOURNEY, saveProgressForm.getEmailAddress(), cipher);

    log.debug("Sending application saved email.");
    messageService.sendApplicationSavedEmail(
        saveProgressForm.getEmailAddress(),
        redisService.getExpiryTimeFormatted(JOURNEY, saveProgressForm.getEmailAddress()));
    log.info("Session saved for return.");
    return REDIRECT + Mappings.URL_PROGRESS_SAVED;
  }

  @Override
  public StepDefinition getStepDefinition() {
    // This is not a real step controller.  Implements to get hold of session (journey).
    return null;
  }
}
