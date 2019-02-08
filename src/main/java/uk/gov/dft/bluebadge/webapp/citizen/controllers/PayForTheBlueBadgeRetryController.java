package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBlueBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBlueBadgeRetryForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

import javax.validation.Valid;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_PAY_FOR_THE_BLUE_BADGE_RETRY)
public class PayForTheBlueBadgeRetryController implements StepController {

  private static final String TEMPLATE = "pay-for-the-blue-badge-retry";

  private final PaymentService paymentService;

  private final RouteMaster routeMaster;

  @Autowired
  PayForTheBlueBadgeRetryController(PaymentService paymentService, RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
    this.paymentService = paymentService;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @ModelAttribute(FORM_REQUEST) PayForTheBlueBadgeRetryForm formRequest) {

    /*
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }*/

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, PayForTheBlueBadgeRetryForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PayForTheBlueBadgeRetryForm formRequest) {

    if (formRequest.getRetry()) {
      String language = journey.getNation().equals(Nation.WLS) ? "cy" : null;
      // TODO: The payment message should be english or welsh
      String returnUrl =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path(Mappings.URL_PAY_FOR_THE_BLUE_BADGE_RETURN)
              .toUriString();
      NewPaymentResponse response =
          paymentService.createPayment(
              journey.getLocalAuthority().getShortCode(), returnUrl, "payment message", language);

      journey.setNewPaymentResponse(response);
      journey.setPaymentJourneyUuid(response.getPaymentJourneyUuid());
      journey.setFormForStep(formRequest);
      return "redirect:" + response.getNextUrl();
    } else {
      journey.setFormForStep(formRequest);
      return routeMaster.redirectToOnSuccess(formRequest, journey);
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PAY_FOR_THE_BLUE_BADGE;
  }
}
