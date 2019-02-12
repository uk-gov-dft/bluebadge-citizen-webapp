package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping(Mappings.URL_PAY_FOR_THE_BADGE_RETURN)
public class PayForTheBadgeReturnController implements StepController {

  private final ApplicationManagementService applicationService;

  private final PaymentService paymentService;

  private final RouteMaster routeMaster;

  @Autowired
  PayForTheBadgeReturnController(
      ApplicationManagementService applicationService,
      PaymentService paymentService,
      RouteMaster routeMaster) {
    this.applicationService = applicationService;
    this.paymentService = paymentService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PayForTheBadgeReturnForm formRequest) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (journey.getPaymentJourneyUuid() == null) {
      return "redirect:" + Mappings.URL_PAY_FOR_THE_BADGE;
    }
    PaymentStatusResponse paymentStatusResponse =
        paymentService.retrievePaymentStatus(journey.getPaymentJourneyUuid());
    journey.setPaymentStatusResponse(paymentStatusResponse);

    if (journey.isPaymentSuccessful()) {
      applicationService.create(JourneyToApplicationConverter.convert(journey));
    }
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PAY_FOR_THE_BADGE_RETURN;
  }
}
