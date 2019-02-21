package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentRetryForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping(Mappings.URL_BADGE_PAYMENT_RETRY)
public class BadgePaymentRetryController implements StepController {

  private static final String TEMPLATE = "badge-payment-retry";

  private final RouteMaster routeMaster;
  private final PaymentService paymentService;
  private final ApplicationManagementService applicationService;

  @Autowired
  BadgePaymentRetryController(
      PaymentService paymentService,
      ApplicationManagementService applicationService,
      RouteMaster routeMaster) {
    this.paymentService = paymentService;
    this.applicationService = applicationService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute(FORM_REQUEST) BadgePaymentRetryForm formRequest) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, BadgePaymentRetryForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) BadgePaymentRetryForm formRequest) {

    if ("yes".equalsIgnoreCase(formRequest.getRetry())) {
      PaymentResponse response = createPayment(journey);
      journey.setPaymentJourneyUuid(response != null ? response.getPaymentJourneyUuid() : null);
      journey.setFormForStep(formRequest);
      if (response == null) {
        return TEMPLATE;
      } else {
        return "redirect:" + response.getNextUrl();
      }
    } else {
      applicationService.create(JourneyToApplicationConverter.convert(journey));
      journey.setFormForStep(formRequest);
      return routeMaster.redirectToOnSuccess(formRequest, journey);
    }
  }

  @ModelAttribute("retryOptions")
  RadioOptionsGroup getRetryOptions() {
    RadioOption yes = new RadioOption("yes", "badgePaymentRetryPage.retry.option.yes");
    RadioOption no = new RadioOption("no", "badgePaymentRetryPage.retry.option.no");

    return new RadioOptionsGroup("badgePaymentRetryPage.title", Lists.newArrayList(yes, no));
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.BADGE_PAYMENT;
  }

  private PaymentResponse createPayment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    String returnUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(Mappings.URL_BADGE_PAYMENT_RETURN)
            .toUriString();
    return paymentService.createPayment(journey.getLocalAuthority().getShortCode(), returnUrl);
  }
}