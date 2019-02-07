package uk.gov.dft.bluebadge.webapp.citizen.service;

import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

@Service
public class PaymentService {
  private PaymentApiClient client;

  public PaymentService(PaymentApiClient paymentServiceApiClient) {
    client = paymentServiceApiClient;
  }

  public NewPaymentResponse createPayment(
      final String laShortCode,
      final String returnUrl,
      final String paymentMessage,
      final String language) {
    NewPaymentRequest request =
        NewPaymentRequest.builder()
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
