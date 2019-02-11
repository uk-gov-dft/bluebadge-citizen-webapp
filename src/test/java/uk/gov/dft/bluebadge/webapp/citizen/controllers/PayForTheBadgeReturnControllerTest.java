package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public class PayForTheBadgeReturnControllerTest {

  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String PAYMENT_STATUS_CREATED = "created";
  private static final String PAYMENT_STATUS_SUCCESS = "success";
  private static final String PAYMENT_STATUS_FAILED = "failed";
  private static final String PAYMENT_REFERENCE = "payref1010";

  NewPaymentResponse NEW_PAYMENT_RESPONSE =
      NewPaymentResponse.builder()
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

  private MockMvc mockMvc;
  @Mock private ApplicationManagementService applicationManagementServiceMock;
  @Mock private PaymentService paymentServiceMock;

  private Journey journey;
  private PayForTheBadgeReturnForm formRequest;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    PayForTheBadgeReturnController controller =
        new PayForTheBadgeReturnController(
            applicationManagementServiceMock, paymentServiceMock, new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PAY_FOR_THE_BADGE, EligibilityCodeField.DLA, true);
    formRequest = PayForTheBadgeReturnForm.builder().build();
  }

  @Test
  public void show_shouldRedirectBackToComplete_whenIsNotValidState() throws Exception {
    journey =
        JourneyFixture.getDefaultJourneyToStep(StepDefinition.NINO, EligibilityCodeField.DLA, true);
    mockMvc
        .perform(get("/pay-for-the-badge-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  public void show_shouldRedirectToPayForTheBadge_whenThereIsNoPaymentJourneyUuidInJourney()
      throws Exception {
    mockMvc
        .perform(get("/pay-for-the-badge-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/pay-for-the-badge"));
  }

  @Test
  public void
      show_shouldRedirectToApplicationSubmittedAndCreateApplication_whenPaymentIsSuccessful()
          throws Exception {

    journey.setNewPaymentResponse(NEW_PAYMENT_RESPONSE);
    journey.setFormForStep(PayForTheBadgeForm.builder().build());

    when(paymentServiceMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .thenReturn(CREATED_PAYMENT_STATUS_RESPONSE)
        .thenReturn(SUCCESS_PAYMENT_STATUS_RESPONSE);

    mockMvc
        .perform(get("/pay-for-the-badge-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));
    verify(applicationManagementServiceMock).create(any());
  }

  @Test
  public void
      show_shouldRedirectToApplicationSubmittedAndCreateApplication_whenPaymentIsNotSuccessful()
          throws Exception {

    journey.setNewPaymentResponse(NEW_PAYMENT_RESPONSE);
    journey.setFormForStep(PayForTheBadgeForm.builder().build());

    when(paymentServiceMock.retrievePaymentStatus(PAYMENT_JOURNEY_UUID))
        .thenReturn(CREATED_PAYMENT_STATUS_RESPONSE)
        .thenReturn(CREATED_PAYMENT_STATUS_RESPONSE)
        .thenReturn(CREATED_PAYMENT_STATUS_RESPONSE)
        .thenReturn(CREATED_PAYMENT_STATUS_RESPONSE)
        .thenReturn(FAILED_PAYMENT_STATUS_RESPONSE);

    mockMvc
        .perform(get("/pay-for-the-badge-return").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/pay-for-the-badge-retry"));
    verify(applicationManagementServiceMock, never()).create(any());
  }

  /*
  @Test
  public void show_ShouldDisplayPayForTheBlueBadgeTemplate() throws Exception {

    mockMvc
        .perform(get("/pay-for-the-badge").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("pay-for-the-badge"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/pay-for-the-badge"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_shouldRedirectToApplicationSubmitted_WhenPayLater() throws Exception {
    mockMvc
        .perform(get("/pay-for-the-badge-by-pass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));
  }

  @Test
  public void submit_shouldRedirectToNextUrl_WhenPayNow() throws Exception {
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    NewPaymentResponse newPaymentResponse = NewPaymentResponse.builder().data(data).build();
    when(paymentServiceMock.createPayment(
            eq("ABERD"), eq(URL_PAY_FOR_THE_BADGE_RETURN), eq(MESSAGE), isNull()))
        .thenReturn(newPaymentResponse);
    mockMvc
        .perform(post("/pay-for-the-badge").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(NEXT_URL));
    verify(paymentServiceMock).createPayment(any(), eq(URL_PAY_FOR_THE_BADGE_RETURN), any(), any());
    assertThat(journey.getPaymentJourneyUuid()).isEqualTo(PAYMENT_JOURNEY_UUID);
  }*/
}
