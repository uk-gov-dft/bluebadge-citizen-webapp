package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class HigherRateMobilityFormTest {
  @Test
  public void determineNextStep_whenYes_thenEligible() {
    HigherRateMobilityForm form =
        HigherRateMobilityForm.builder().awardedHigherRateMobility(true).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenNo_thenMainReason() {
    HigherRateMobilityForm form =
        HigherRateMobilityForm.builder().awardedHigherRateMobility(false).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.MAIN_REASON);
  }
}
