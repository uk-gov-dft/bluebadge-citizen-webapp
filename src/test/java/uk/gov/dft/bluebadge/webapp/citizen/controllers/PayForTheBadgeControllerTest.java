package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.NewPaymentResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public class PayForTheBadgeControllerTest {

  private static final String URL_PAY_FOR_THE_BADGE_RETURN =
      "http://localhost/pay-for-the-badge-return";
  private static final String NEXT_URL = "http://localhost/next-url";
  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String MESSAGE = "My message";

  private MockMvc mockMvc;
  @Mock private ApplicationManagementService applicationServiceMock;
  @Mock private PaymentService paymentServiceMock;
  @Mock private MessageSource messageSourceMock;

  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    PayForTheBadgeController controller =
        new PayForTheBadgeController(
            paymentServiceMock, applicationServiceMock, new RouteMaster(), messageSourceMock);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PAY_FOR_THE_BADGE, EligibilityCodeField.DLA, true);

    when(messageSourceMock.getMessage(
            eq("payForTheBlueBadgePage.api.createPayment.paymentMessage"), isNull(), any()))
        .thenReturn(MESSAGE);
  }

  @Test
  public void show_ShouldDisplayPayForTheBlueBadgeTemplate() throws Exception {
    PayForTheBadgeForm formRequest = PayForTheBadgeForm.builder().build();

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
    verify(applicationServiceMock).create(any());
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
  }
}
