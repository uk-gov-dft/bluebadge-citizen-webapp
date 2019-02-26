package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.webapp.citizen.client.CommonResponseErrorHandler;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.PaymentApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;

public class PaymentApiClientTest {
  private static final String TEST_URI = "http://justtesting:8787/test";
  private static final String APPLICATION_ENDPOINT = "/payments";
  public static final String PAYMENT_JOURNEY_UUID = "paymentJourneyUuid1";

  private MockRestServiceServer mockServer;

  private ObjectMapper objectMapper = new ObjectMapper();
  private PaymentApiClient client;

  @Before
  public void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new CommonResponseErrorHandler(objectMapper));
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    client = new PaymentApiClient(restTemplate);
  }

  @Test
  public void createPayment_shouldWork() throws Exception {
    PaymentRequest request =
        PaymentRequest.builder()
            .language(null)
            .laShortCode("ABERD")
            .paymentMessage("payment message")
            .returnUrl("http://localhost/return-url")
            .build();
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", "http://localhost/next-url");

    PaymentResponse response = PaymentResponse.builder().data(data).build();
    String jsonResponse = objectMapper.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(TEST_URI + APPLICATION_ENDPOINT))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

    assertThat(client.createPayment(request)).isEqualTo(response);
  }

  @Test
  public void retrievePaymentStatus_shouldWork() throws Exception {
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", "paymentJourneyUuid1");
    data.put("status", "created");
    data.put("reference", "myref1");
    PaymentStatusResponse response = PaymentStatusResponse.builder().data(data).build();
    String jsonResponse = objectMapper.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(TEST_URI + APPLICATION_ENDPOINT + "/" + PAYMENT_JOURNEY_UUID))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

    assertThat(client.retrievePaymentStatus(PAYMENT_JOURNEY_UUID)).isEqualTo(response);
  }
}
