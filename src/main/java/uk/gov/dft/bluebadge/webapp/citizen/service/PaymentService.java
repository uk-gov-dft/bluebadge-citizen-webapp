package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

@Slf4j
@Service
public class PaymentService {
  private PaymentApiClient client;
  private MessageSource messageSource;

  public PaymentService(PaymentApiClient paymentServiceApiClient, MessageSource messageSource) {
    this.client = paymentServiceApiClient;
    this.messageSource = messageSource;
  }

  public PaymentResponse createPayment(final String laShortCode, final String returnUrl) {
    try {
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

      PaymentResponse paymentResponse = client.createPayment(request);
      return paymentResponse;
    } catch (RestClientException ex) {
      log.error("Exception creating payment for la [{}]: [{}]", laShortCode, ex);
      return null;
    }
  }

  public PaymentStatusResponse retrievePaymentStatus(String paymentJourneyUuid) {
    return client.retrievePaymentStatus(paymentJourneyUuid);
  }
}
