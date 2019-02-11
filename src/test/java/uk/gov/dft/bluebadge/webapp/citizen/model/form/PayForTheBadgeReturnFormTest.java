package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class PayForTheBadgeReturnFormTest {

  private static final String PAYMENT_JOURNEY_UUID = "journeypay1";
  private static final String PAYMENT_STATUS_SUCCESS = "success";
  private static final String PAYMENT_STATUS_FAILED = "failed";
  private static final String PAYMENT_REFERENCE = "payref1010";

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

  @Test
  public void determineNextStep_shouldReturnSubmitted_whenPaymentWasSuccessful() {
    PayForTheBadgeReturnForm form = PayForTheBadgeReturnForm.builder().build();
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PAY_FOR_THE_BADGE, EligibilityCodeField.PIP, true);

    journey.setPaymentStatusResponse(SUCCESS_PAYMENT_STATUS_RESPONSE);

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.SUBMITTED);
  }

  @Test
  public void determineNextStep_shouldReturnSubmitted_whenPaymentWasNotSuccessful() {
    PayForTheBadgeReturnForm form = PayForTheBadgeReturnForm.builder().build();
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PAY_FOR_THE_BADGE, EligibilityCodeField.PIP, true);

    journey.setPaymentStatusResponse(FAILED_PAYMENT_STATUS_RESPONSE);

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get())
        .isEqualTo(StepDefinition.PAY_FOR_THE_BADGE_RETRY);
  }
}
