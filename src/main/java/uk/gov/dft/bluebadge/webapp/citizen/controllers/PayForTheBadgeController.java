package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_PAY_FOR_THE_BADGE_BYPASS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping
public class PayForTheBadgeController extends PayForTheBadgeBaseController {

  private static final String TEMPLATE = "pay-for-the-badge";

  private final ApplicationManagementService applicationService;

  private final RouteMaster routeMaster;

  @Autowired
  PayForTheBadgeController(
      PaymentService paymentService,
      ApplicationManagementService applicationService,
      RouteMaster routeMaster,
      MessageSource messageSource) {
    super(paymentService, messageSource);
    this.applicationService = applicationService;
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_PAY_FOR_THE_BADGE)
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute(FORM_REQUEST) PayForTheBadgeForm formRequest) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, PayForTheBadgeForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping(Mappings.URL_PAY_FOR_THE_BADGE)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PayForTheBadgeForm formRequest) {
    NewPaymentResponse response = createPayment(journey);
    journey.setNewPaymentResponse(response);
    journey.setFormForStep(formRequest);
    return "redirect:" + response.getNextUrl();
  }

  @GetMapping(URL_PAY_FOR_THE_BADGE_BYPASS)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    applicationService.create(JourneyToApplicationConverter.convert(journey));
    PayForTheBadgeForm formRequest = PayForTheBadgeForm.builder().build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PAY_FOR_THE_BADGE;
  }
}
