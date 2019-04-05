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
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NotPaidForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

@Controller
@RequestMapping(Mappings.URL_NOT_PAID)
public class NotPaidController implements StepController {

  private static final String TEMPLATE = "not-paid";

  private final RouteMaster routeMaster;
  private final PaymentService paymentService;
  private final ApplicationManagementService applicationService;

  @Autowired
  NotPaidController(
      PaymentService paymentService,
      ApplicationManagementService applicationService,
      RouteMaster routeMaster) {
    this.paymentService = paymentService;
    this.applicationService = applicationService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      NotPaidForm formRequest = journey.getFormForStep(getStepDefinition());
      formRequest.setRetry(null);
      model.addAttribute(FORM_REQUEST, formRequest);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, NotPaidForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) NotPaidForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    if ("yes".equalsIgnoreCase(formRequest.getRetry())) {
      PaymentResponse response = createPayment(journey);
      journey.setPaymentJourneyUuid(response != null ? response.getPaymentJourneyUuid() : null);
      journey.setFormForStep(formRequest);
      if (response == null) {
        return "redirect:" + Mappings.URL_NOT_PAID;
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
    RadioOption yes = new RadioOption("yes", "notPaidPage.retry.option.yes");
    RadioOption no = new RadioOption("no", "notPaidPage.retry.option.no");
    return new RadioOptionsGroup("notPaidPage.content.title", Lists.newArrayList(yes, no));
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.NOT_PAID;
  }

  private PaymentResponse createPayment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    String returnUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(Mappings.URL_BADGE_PAYMENT_RETURN)
            .toUriString();
    return paymentService.createPayment(journey.getLocalAuthority().getShortCode(), returnUrl);
  }
}
