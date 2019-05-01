package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveApplicationForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

@Slf4j
@Controller
@RequestMapping("/save-application")
public class SaveApplicationController implements StepController, SaveAndReturnController {
  private static final String TEMPLATE = "save-and-return/save-application";
  public static final String FORM_REQUEST = "formRequest";
  private RedisService redisService;
  private CryptoService cryptoService;

  @Autowired
  public SaveApplicationController(RedisService redisService, CryptoService cryptoService) {
    this.redisService = redisService;
    this.cryptoService = cryptoService;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!model.containsAttribute("formRequest")) {
      ContactDetailsForm contactDetailsForm =
          journey.getFormForStep(StepDefinition.CONTACT_DETAILS);
      EnterAddressForm enterAddressForm = journey.getFormForStep(StepDefinition.ADDRESS);
      String postcode = null;
      String emailAddress = null;
      if (null != enterAddressForm && null != enterAddressForm.getPostcode()) {
        postcode = enterAddressForm.getPostcode();
      }
      if (null != contactDetailsForm && null != contactDetailsForm.getEmailAddress()) {
        emailAddress = contactDetailsForm.getEmailAddress();
      }
      model.addAttribute(
          "formRequest",
          SaveApplicationForm.builder().emailAddress(emailAddress).postcode(postcode).build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) SaveApplicationForm saveApplicationForm,
      HttpServletRequest request,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return redirectToOnBindingError(Mappings.URL_RETURN_TO_APPLICATION, saveApplicationForm, bindingResult, attr);
    }

    String cipher = cryptoService.encryptJourney(journey, saveApplicationForm.getPostcode());
    redisService.setAndExpireIfNew(JOURNEY, saveApplicationForm.getEmailAddress(), cipher);
    log.info("Session saved for return.");
    return REDIRECT + Mappings.URL_TASK_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return null;
  }
}
