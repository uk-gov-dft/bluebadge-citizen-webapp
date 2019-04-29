package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.SerializationUtils;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Base64;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping("/save-application")
public class SaveApplicationController implements StepController {
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

    model.addAttribute("formRequest", SaveAndReturnForm.builder().build());
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) SaveAndReturnForm saveAndReturnForm,
      HttpServletRequest request,
      BindingResult bindingResult,
      RedirectAttributes attr) throws CryptoVersionException {

journey.setPaymentJourneyUuid("AWSE");
    String cipher = cryptoService.encryptJourney(journey);
    redisService.setEncryptedJourneyForReturn(saveAndReturnForm.getEmailAddress(), cipher);
//redisService.setEncryptedJourneyForReturn(saveAndReturnForm.getEmailAddress(), Base64.getEncoder().encodeToString(SerializationUtils.serialize(journey)));
    Journey read = cryptoService.decryptJourney(redisService.getEncryptedJourneyOnReturn(saveAndReturnForm.getEmailAddress()), "1.0.0");

    request.getSession().setAttribute(JOURNEY_SESSION_KEY, read);
    return REDIRECT + Mappings.URL_TASK_LIST;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return null;
  }
}
