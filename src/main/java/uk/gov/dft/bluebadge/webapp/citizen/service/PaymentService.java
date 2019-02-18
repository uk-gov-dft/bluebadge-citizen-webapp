package uk.gov.dft.bluebadge.webapp.citizen.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

@Service
public class PaymentService {
  private PaymentApiClient client;
  private MessageSource messageSource;

  public PaymentService(PaymentApiClient paymentServiceApiClient, MessageSource messageSource) {
    this.client = paymentServiceApiClient;
    this.messageSource = messageSource;
  }

  public PaymentResponse createPayment(final String laShortCode, final String returnUrl) {
    String paymentMessage =
        messageSource.getMessage(
            "badgePaymentPage.api.createPayment.paymentMessage",
            null,
            LocaleContextHolder.getLocale());
    String language =
        LocaleContextHolder.getLocale().getLanguage().equalsIgnoreCase("cy") ? "cy" : null;

    PaymentRequest request =
        PaymentRequest.builder()
            .laShortCode(laShortCode)
            .returnUrl(returnUrl)
            .paymentMessage(paymentMessage)
            .language(language)
            .build();

    return client.createPayment(request);
  }

  public PaymentStatusResponse retrievePaymentStatus(String paymentJourneyUuid) {
    return client.retrievePaymentStatus(paymentJourneyUuid);
  }
}
