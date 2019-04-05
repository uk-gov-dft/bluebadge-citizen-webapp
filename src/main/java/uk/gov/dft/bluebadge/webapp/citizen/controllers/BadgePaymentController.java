package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_BADGE_PAYMENT_BYPASS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping
public class BadgePaymentController implements StepController {

  private static final String TEMPLATE = "badge-payment";

  private final PaymentService paymentService;
  private final ApplicationManagementService applicationService;

  private final RouteMaster routeMaster;

  @Autowired
  BadgePaymentController(
      PaymentService paymentService,
      ApplicationManagementService applicationService,
      RouteMaster routeMaster) {
    this.paymentService = paymentService;
    this.applicationService = applicationService;
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_BADGE_PAYMENT)
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute(FORM_REQUEST) BadgePaymentForm formRequest) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, BadgePaymentForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping(Mappings.URL_BADGE_PAYMENT)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) BadgePaymentForm formRequest) {
    PaymentResponse response = createPayment(journey);
    journey.setPaymentJourneyUuid(response != null ? response.getPaymentJourneyUuid() : null);
    journey.setFormForStep(formRequest);
    if (response == null) {
      return "redirect:" + Mappings.URL_NOT_PAID;
    } else {
      return "redirect:" + response.getNextUrl();
    }
  }

  @GetMapping(URL_BADGE_PAYMENT_BYPASS)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    applicationService.create(JourneyToApplicationConverter.convert(journey));
    BadgePaymentForm formRequest = BadgePaymentForm.builder().payNow(false).build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private PaymentResponse createPayment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    String returnUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(Mappings.URL_BADGE_PAYMENT_RETURN)
            .toUriString();
    return paymentService.createPayment(journey.getLocalAuthority().getShortCode(), returnUrl);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.BADGE_PAYMENT;
  }
}
