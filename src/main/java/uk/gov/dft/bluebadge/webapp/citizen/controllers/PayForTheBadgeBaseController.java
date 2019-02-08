package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public abstract class PayForTheBadgeBaseController implements StepController {
  protected MessageSource messageSource;

  protected final PaymentService paymentService;

  protected PayForTheBadgeBaseController(
      PaymentService paymentService, MessageSource messageSource) {
    this.paymentService = paymentService;
    this.messageSource = messageSource;
  }

  protected NewPaymentResponse createPayment(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    String returnUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(Mappings.URL_PAY_FOR_THE_BADGE_RETURN)
            .toUriString();
    String paymentMessage =
        messageSource.getMessage(
            "payForTheBlueBadgePage.api.createPayment.paymentMessage",
            null,
            LocaleContextHolder.getLocale());
    String language =
        LocaleContextHolder.getLocale().getLanguage().equalsIgnoreCase("cy") ? "cy" : null;

    return paymentService.createPayment(
        journey.getLocalAuthority().getShortCode(), returnUrl, paymentMessage, language);
  }
}
