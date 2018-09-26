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

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenDLA_thenEligible() {
    ReceiveBenefitsForm form =
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.DLA).build();

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.HIGHER_RATE_MOBILITY);
  }

  @Test
  public void determineNextStep_whenAnythingElse_thenMayBeEligible() {

    EnumSet.complementOf(EnumSet.of(EligibilityCodeField.WPMS, EligibilityCodeField.DLA))
        .forEach(
            e -> {
              ReceiveBenefitsForm form = ReceiveBenefitsForm.builder().benefitType(e).build();

              assertThat(form.determineNextStep()).isNotEmpty();
              assertThat(form.determineNextStep().get())
                  .as("Eligibility %s result in Eligible", e)
                  .isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
            });
  }

  @Test
  public void determineNextStep_whenPIP_thenPipovingAround() {

    EnumSet.of(EligibilityCodeField.PIP)
        .forEach(
            e -> {
              ReceiveBenefitsForm form = ReceiveBenefitsForm.builder().benefitType(e).build();

              assertThat(form.determineNextStep()).isNotEmpty();
              assertThat(form.determineNextStep().get())
                  .as("Eligibility %s result in Eligible", e)
                  .isEqualTo(StepDefinition.PIP_MOVING_AROUND);
            });
  }
}
