package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestClientException;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

public class PaymentServiceTest {

  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String MESSAGE = "My message";
  public static final String LANGUAGE = "en";
  public static final String LA_SHORT_CODE = "ABERD";
  private static final String PAYMENT_REFERENCE = "payRef1";

  @Mock private PaymentApiClient clientMock;
  @Mock private MessageSource messageSourceMock;

  private PaymentService paymentService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    paymentService = new PaymentService(clientMock, messageSourceMock);

    when(messageSourceMock.getMessage(
            eq("badgePaymentPage.api.createPayment.paymentMessage"), isNull(), any()))
        .thenReturn(MESSAGE);

    Locale english = new Locale("en");
    LocaleContextHolder.setLocale(english);
  }

  @Test
  public void createPayment_shouldReturnResponse_whenSuccessul() {
    PaymentRequest paymentRequest =
        PaymentRequest.builder()
            .language(null)
            .laShortCode(LA_SHORT_CODE)
            .paymentMessage(MESSAGE)
            .returnUrl(NEXT_URL)
            .build();

    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    PaymentResponse paymentResponse = PaymentResponse.builder().data(data).build();

    when(clientMock.createPayment(paymentRequest)).thenReturn(paymentResponse);

    assertThat(paymentService.createPayment(LA_SHORT_CODE, NEXT_URL)).isEqualTo(paymentResponse);
  }

  @Test
  public void createPayment_shouldReturnNull_whenClientThrowsException() {
    PaymentRequest paymentRequest =
        PaymentRequest.builder()
            .language(null)
            .laShortCode(LA_SHORT_CODE)
            .paymentMessage(MESSAGE)
            .returnUrl(NEXT_URL)
            .build();

    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    PaymentResponse paymentResponse = PaymentResponse.builder().data(data).build();

    when(clientMock.createPayment(paymentRequest))
        .thenThrow(new RestClientException("Rest client exception"));

    assertThat(paymentService.createPayment(LA_SHORT_CODE, NEXT_URL)).isNull();
    verify(clientMock).createPayment(paymentRequest);
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
