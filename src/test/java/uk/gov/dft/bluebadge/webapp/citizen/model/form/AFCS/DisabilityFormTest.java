package uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class DisabilityFormTest {

  @Test
  public void determineNextStep_whenDisabilityIsYes_thenEligible() {
    DisabilityForm form = DisabilityForm.builder().hasDisability(true).build();

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenDisabilityIsNo_thenMaybeEligible() {

    DisabilityForm form = DisabilityForm.builder().hasDisability(false).build();

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
  }
}
