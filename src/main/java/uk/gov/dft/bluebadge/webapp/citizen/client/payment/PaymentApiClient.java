package uk.gov.dft.bluebadge.webapp.citizen.client.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

@Slf4j
@Service
public class PaymentApiClient {

  private final RestTemplate restTemplate;

  @Autowired
  public PaymentApiClient(
      @Qualifier("paymentServiceRestTemplate") RestTemplate paymentServiceRestTemplate) {
    this.restTemplate = paymentServiceRestTemplate;
  }

  /**
   * Create payment.
   *
   * @param newPaymentRequest
   * @return NewPaymentResponse with paymentJourneyUuid and url to redirect to.
   */
  public NewPaymentResponse createPayment(final NewPaymentRequest newPaymentRequest) {
    log.debug("Create payment.");

    HttpEntity<NewPaymentRequest> request = new HttpEntity<>(newPaymentRequest);

    return restTemplate
        .postForEntity(
            UriComponentsBuilder.newInstance().path("/payments").toUriString(),
            request,
            NewPaymentResponse.class)
        .getBody();
  }

  /**
   * Retrieve payment status.
   *
   * @param paymentJourneyUuid
   * @return PaymentStatusResponse
   */
  public PaymentStatusResponse retrievePaymentStatus(String paymentJourneyUuid) {
    log.debug("retrieve payment status.");

    return restTemplate
        .getForEntity(
            UriComponentsBuilder.newInstance()
                .path("/payments/")
                .path(paymentJourneyUuid)
                .toUriString(),
            PaymentStatusResponse.class)
        .getBody();
  }
}
