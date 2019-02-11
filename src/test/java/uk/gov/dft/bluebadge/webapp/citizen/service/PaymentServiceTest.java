package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

public class PaymentServiceTest {

  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String MESSAGE = "My message";
  public static final String LANGUAGE = "en";
  public static final String LA_SHORT_CODE = "ABERD";
  private static final String PAYMENT_REFERENCE = "payRef1";

  @Mock private PaymentApiClient clientMock;

  private PaymentService paymentService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    paymentService = new PaymentService(clientMock);
  }

  @Test
  public void createPayment_shouldWork() {
    NewPaymentRequest newPaymentRequest =
        NewPaymentRequest.builder()
            .language(LANGUAGE)
            .laShortCode(LA_SHORT_CODE)
            .paymentMessage(MESSAGE)
            .returnUrl(NEXT_URL)
            .build();

    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    NewPaymentResponse newPaymentResponse = NewPaymentResponse.builder().data(data).build();

    when(clientMock.createPayment(newPaymentRequest)).thenReturn(newPaymentResponse);

    assertThat(paymentService.createPayment(LA_SHORT_CODE, NEXT_URL, MESSAGE, LANGUAGE))
        .isEqualTo(newPaymentResponse);
  }

  @Test
  public void retrievePaymentStatus_shouldWork() {
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("status", NEXT_URL);
    data.put("reference", PAYMENT_REFERENCE);

    PaymentStatusResponse paymentStatusResponse =
        PaymentStatusResponse.builder().data(data).build();

    when(clientMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID)).thenReturn(paymentStatusResponse);

    assertThat(paymentService.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .isEqualTo(paymentStatusResponse);
  }
}
