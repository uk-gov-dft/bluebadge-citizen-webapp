package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PaymentUnavailableForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping
public class PaymentUnavailableController implements StepController {

  private static final String TEMPLATE = "payment-unavailable";

  private final RouteMaster routeMaster;
  private final PaymentService paymentService;
  private final ApplicationManagementService applicationService;

  @Autowired
  PaymentUnavailableController(
      PaymentService paymentService,
      ApplicationManagementService applicationService,
      RouteMaster routeMaster) {
    this.paymentService = paymentService;
    this.applicationService = applicationService;
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_PAYMENT_UNAVAILABLE)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      PaymentUnavailableForm formRequest = journey.getFormForStep(getStepDefinition());
      model.addAttribute(FORM_REQUEST, formRequest);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, PaymentUnavailableForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping(Mappings.URL_PAYMENT_UNAVAILABLE)
  public String submitAndPayLater(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PaymentUnavailableForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    applicationService.create(JourneyToApplicationConverter.convert(journey));
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @GetMapping(Mappings.URL_PAYMENT_UNAVAILABLE_RETRY)
  public String payAndSubmit(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    PaymentResponse response = createPayment(journey);
    journey.setPaymentJourneyUuid(response != null ? response.getPaymentJourneyUuid() : null);
    if (response == null) {
      return "redirect:" + Mappings.URL_PAYMENT_UNAVAILABLE;
    } else {
      return "redirect:" + response.getNextUrl();
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PAYMENT_UNAVAILABLE;
  }

  private PaymentResponse createPayment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    String returnUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(Mappings.URL_BADGE_PAYMENT_RETURN)
            .toUriString();
    return paymentService.createPayment(journey.getLocalAuthority().getShortCode(), returnUrl);
  }
}
