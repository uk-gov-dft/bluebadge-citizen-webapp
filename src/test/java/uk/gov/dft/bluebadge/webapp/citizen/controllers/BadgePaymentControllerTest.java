package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public class BadgePaymentControllerTest {

  private static final String URL_BADGE_PAYMENT_RETURN = "http://localhost/badge-payment-return";
  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";

  private MockMvc mockMvc;
  @Mock private ApplicationManagementService applicationServiceMock;
  @Mock private PaymentService paymentServiceMock;

  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    BadgePaymentController controller =
        new BadgePaymentController(paymentServiceMock, applicationServiceMock, new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.BADGE_PAYMENT, EligibilityCodeField.DLA, true);
  }

  @Test
  public void show_ShouldDisplayBadgePaymentTemplate() throws Exception {
    BadgePaymentForm formRequest = BadgePaymentForm.builder().build();

    mockMvc
        .perform(get("/badge-payment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("badge-payment"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/badge-payment"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void formByPass_shouldRedirectToApplicationSubmitted_WhenPayLater() throws Exception {
    mockMvc
        .perform(get("/badge-payment-by-pass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));
    BadgePaymentForm expectedBadgePaymentFormRequest =
        BadgePaymentForm.builder().payNow(false).build();
    assertThat((BadgePaymentForm) journey.getFormForStep(StepDefinition.BADGE_PAYMENT))
        .isEqualTo(expectedBadgePaymentFormRequest);
    verify(applicationServiceMock).create(any());
  }

  @Test
  public void submit_shouldRedirectToNextUrl_WhenPayNow() throws Exception {
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    PaymentResponse paymentResponse = PaymentResponse.builder().data(data).build();
    when(paymentServiceMock.createPayment(eq("ABERD"), eq(URL_BADGE_PAYMENT_RETURN)))
        .thenReturn(paymentResponse);
    mockMvc
        .perform(post("/badge-payment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(NEXT_URL));
    verify(paymentServiceMock).createPayment(any(), eq(URL_BADGE_PAYMENT_RETURN));
    assertThat(journey.getPaymentJourneyUuid()).isEqualTo(PAYMENT_JOURNEY_UUID);
  }

  @Test
  public void submit_shouldRedirectToNotPaid_WhenPaymentIsNotSuccessful() throws Exception {
    Map<String, String> data = new HashMap<>();
    data.put("paymentJourneyUuid", PAYMENT_JOURNEY_UUID);
    data.put("nextUrl", NEXT_URL);
    PaymentResponse paymentResponse = PaymentResponse.builder().data(data).build();
    when(paymentServiceMock.createPayment(eq("ABERD"), eq(URL_BADGE_PAYMENT_RETURN)))
        .thenReturn(null);
    mockMvc
        .perform(post("/badge-payment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/not-paid"));
    verify(paymentServiceMock).createPayment(any(), eq(URL_BADGE_PAYMENT_RETURN));
    assertThat(journey.getPaymentJourneyUuid()).isNull();
  }
}
