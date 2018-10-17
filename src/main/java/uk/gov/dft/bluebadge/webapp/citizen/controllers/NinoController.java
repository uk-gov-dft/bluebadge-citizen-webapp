package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

@Controller
public class NinoController implements StepController {

  public static final String TEMPLATE = "nino";
  public static final String FORM_REQUEST = "formRequest";
  public static final String NINO_BYPASS_URL = "/nino-bypass";

  private final RouteMaster routeMaster;

  @Autowired
  public NinoController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_NINO)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getNinoForm()) {
      model.addAttribute(FORM_REQUEST, journey.getNinoForm());
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, NinoForm.builder().build());
    }

    return TEMPLATE;
  }

  @GetMapping(NINO_BYPASS_URL)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    NinoForm formRequest = NinoForm.builder().build();
    journey.setNinoForm(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest);
  }

  @PostMapping(Mappings.URL_NINO)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) NinoForm ninoForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, ninoForm, bindingResult, attr);
    }

    journey.setNinoForm(ninoForm);

    return routeMaster.redirectToOnSuccess(ninoForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.NINO;
  }
}
