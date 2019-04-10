package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public class BadgePaymentReturnControllerTest {

  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String PAYMENT_STATUS_CREATED = "created";
  private static final String PAYMENT_STATUS_SUCCESS = "success";
  private static final String PAYMENT_STATUS_FAILED = "failed";
  private static final String PAYMENT_STATUS_UNKWOWN = "unknown";
  private static final String PAYMENT_REFERENCE = "payref1010";

  PaymentResponse PAYMENT_RESPONSE =
      PaymentResponse.builder()
          .data(
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
                  put("nextUrl", NEXT_URL);
                }
              })
          .build();
  PaymentStatusResponse CREATED_PAYMENT_STATUS_RESPONSE =
      PaymentStatusResponse.builder()
          .data(
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
                  put("status", PAYMENT_STATUS_CREATED);
                  put("reference", PAYMENT_REFERENCE);
                }
              })
          .build();
  PaymentStatusResponse SUCCESS_PAYMENT_STATUS_RESPONSE =
      PaymentStatusResponse.builder()
          .data(
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
                  put("status", PAYMENT_STATUS_SUCCESS);
                  put("reference", PAYMENT_REFERENCE);
                }
              })
          .build();
  PaymentStatusResponse FAILED_PAYMENT_STATUS_RESPONSE =
      PaymentStatusResponse.builder()
          .data(
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
                  put("status", PAYMENT_STATUS_FAILED);
                  put("reference", PAYMENT_REFERENCE);
                }
              })
          .build();
  PaymentStatusResponse UNKNOWN_PAYMENT_STATUS_RESPONSE =
      PaymentStatusResponse.builder()
          .data(
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
                  put("status", PAYMENT_STATUS_UNKWOWN);
                  put("reference", null);
                }
              })
          .build();

  private MockMvc mockMvc;
  @Mock private ApplicationManagementService applicationManagementServiceMock;
  @Mock private PaymentService paymentServiceMock;

  private Journey journey;
  private BadgePaymentReturnForm formRequest;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    BadgePaymentReturnController controller =
        new BadgePaymentReturnController(
            applicationManagementServiceMock, paymentServiceMock, RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.BADGE_PAYMENT, EligibilityCodeField.DLA, true);
    formRequest = BadgePaymentReturnForm.builder().build();
  }

  @Test
  public void show_shouldRedirectBackToComplete_whenIsNotValidState() throws Exception {
    journey =
        JourneyFixture.getDefaultJourneyToStep(StepDefinition.NINO, EligibilityCodeField.DLA, true);
    mockMvc
        .perform(get("/badge-payment-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
  }

  @Test
  public void show_shouldRedirectToBadgePayment_whenThereIsNoPaymentJourneyUuidInJourney()
      throws Exception {
    mockMvc
        .perform(get("/badge-payment-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/not-paid"));
  }

  @Test
  public void
      show_shouldRedirectToApplicationSubmittedAndCreateApplicationWithPaymentReference_whenPaymentIsSuccessful()
          throws Exception {

    journey.setPaymentJourneyUuid(PAYMENT_JOURNEY_UUID);
    journey.setFormForStep(BadgePaymentForm.builder().build());

    when(paymentServiceMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .thenReturn(SUCCESS_PAYMENT_STATUS_RESPONSE);

    mockMvc
        .perform(get("/badge-payment-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));

    verify(applicationManagementServiceMock).create(any());
    ArgumentCaptor<Application> argument = ArgumentCaptor.forClass(Application.class);
    verify(applicationManagementServiceMock).create(argument.capture());
    assertThat(argument.getValue().getPaymentReference()).isEqualTo(PAYMENT_REFERENCE);
  }

  @Test
  public void
      show_shouldRedirectToApplicationSubmittedAndCreateApplicationWithoutPaymentReference_whenPaymentStatusCannotBeRetrieved()
          throws Exception {

    journey.setPaymentJourneyUuid(PAYMENT_JOURNEY_UUID);
    journey.setFormForStep(BadgePaymentForm.builder().build());

    when(paymentServiceMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .thenThrow(new RestClientException("Rest client exception"));

    mockMvc
        .perform(get("/badge-payment-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));

    verify(applicationManagementServiceMock).create(any());
    ArgumentCaptor<Application> argument = ArgumentCaptor.forClass(Application.class);
    verify(applicationManagementServiceMock).create(argument.capture());
    assertThat(argument.getValue().getPaymentReference()).isEqualTo("Unknown");
    assertThat(journey.getPaymentStatus()).isEqualTo("unknown");
    assertThat(journey.getPaymentReference()).isEqualTo("Unknown");
    assertThat(journey.isPaymentSuccessful()).isEqualTo(false);
    assertThat(journey.isPaymentStatusUnknown()).isEqualTo(true);
    assertThat(journey.getPaymentJourneyUuid()).isEqualTo(PAYMENT_JOURNEY_UUID);
  }

  @Test
  public void
      show_shouldRedirectToApplicationSubmittedAndCreateApplication_whenPaymentIsNotSuccessful()
          throws Exception {

    journey.setPaymentJourneyUuid(PAYMENT_JOURNEY_UUID);
    journey.setFormForStep(BadgePaymentForm.builder().build());

    when(paymentServiceMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .thenReturn(FAILED_PAYMENT_STATUS_RESPONSE);

    mockMvc
        .perform(get("/badge-payment-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/not-paid"));
    verify(applicationManagementServiceMock, never()).create(any());
  }
}
