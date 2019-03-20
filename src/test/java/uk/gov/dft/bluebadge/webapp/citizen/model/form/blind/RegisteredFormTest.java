package uk.gov.dft.bluebadge.webapp.citizen.model.form.blind;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class RegisteredFormTest {

  @Test
  public void determineNextStep_whenHasRegisteredIsYes_thenEligible() {
    RegisteredForm form = RegisteredForm.builder().hasRegistered(true).build();
    assertThat(form.determineNextStep(null)).isEqualTo(Optional.of(StepDefinition.PERMISSION));
  }

  @Test
  public void determineNextStep_whenHasRegisteredIsNo_thenMainReason() {
    RegisteredForm form = RegisteredForm.builder().hasRegistered(false).build();
    assertThat(form.determineNextStep(null)).isEqualTo(Optional.of(StepDefinition.PROVE_IDENTITY));
  }
}
