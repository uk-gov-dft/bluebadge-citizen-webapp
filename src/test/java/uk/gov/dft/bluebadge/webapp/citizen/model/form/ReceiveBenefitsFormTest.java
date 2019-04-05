package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class ReceiveBenefitsFormTest {
  @Test
  public void determineNextStep_whenWPMS_thenEligible() {
    ReceiveBenefitsForm form =
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.WPMS).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenDLA_thenEligible() {
    ReceiveBenefitsForm form =
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.DLA).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.HIGHER_RATE_MOBILITY);
  }

  @Test
  public void determineNextStep_whenAnythingElse_thenMainReason() {

    EnumSet.complementOf(
            EnumSet.of(
                EligibilityCodeField.WPMS,
                EligibilityCodeField.AFRFCS,
                EligibilityCodeField.PIP,
                EligibilityCodeField.DLA))
        .forEach(
            e -> {
              ReceiveBenefitsForm form = ReceiveBenefitsForm.builder().benefitType(e).build();

              assertThat(form.determineNextStep(null)).isNotEmpty();
              assertThat(form.determineNextStep(null).get())
                  .as("Eligibility %s result in Main Reason", e)
                  .isEqualTo(StepDefinition.MAIN_REASON);
            });
  }

  @Test
  public void determineNextStep_whenPIP_thenPipovingAround() {

    EnumSet.of(EligibilityCodeField.PIP)
        .forEach(
            e -> {
              ReceiveBenefitsForm form = ReceiveBenefitsForm.builder().benefitType(e).build();

              assertThat(form.determineNextStep(null)).isNotEmpty();
              assertThat(form.determineNextStep(null).get())
                  .as("Eligibility %s result in Eligible", e)
                  .isEqualTo(StepDefinition.PIP_MOVING_AROUND);
            });
  }
}
