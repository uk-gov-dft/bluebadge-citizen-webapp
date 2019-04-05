package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class DisabilityFormTest {

  @Test
  public void determineNextStep_whenDisabilityIsYes_thenEligible() {
    DisabilityForm form = DisabilityForm.builder().hasDisability(true).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenDisabilityIsNo_thenMainReason() {

    DisabilityForm form = DisabilityForm.builder().hasDisability(false).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.MAIN_REASON);
  }
}
