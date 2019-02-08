package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping(Mappings.URL_PAY_FOR_THE_BADGE)
public class PayForTheBadgeController extends PayForTheBadgeBaseController {

  private static final String TEMPLATE = "pay-for-the-badge";

  private final RouteMaster routeMaster;

  @Autowired
  PayForTheBadgeController(
      PaymentService paymentService, RouteMaster routeMaster, MessageSource messageSource) {
    super(paymentService, messageSource);
    this.routeMaster = routeMaster;
  }

  @GetMapping
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

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) PayForTheBadgeForm formRequest) {

    if (formRequest.getPayNow()) {
      NewPaymentResponse response = createPayment(journey);
      journey.setNewPaymentResponse(response);
      journey.setFormForStep(formRequest);
      return "redirect:" + response.getNextUrl();
    } else {
      journey.setFormForStep(formRequest);
      return routeMaster.redirectToOnSuccess(formRequest, journey);
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PAY_FOR_THE_BADGE;
  }
}
