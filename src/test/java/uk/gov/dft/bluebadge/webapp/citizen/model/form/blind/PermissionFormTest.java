package uk.gov.dft.bluebadge.webapp.citizen.model.form.blind;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class PermissionFormTest {

  @Test
  public void determineNextStep_whenHasPermissionIsYes_thenEligible() {
    PermissionForm form = PermissionForm.builder().hasPermission(true).build();
    assertThat(form.determineNextStep(null))
        .isEqualTo(Optional.of(StepDefinition.REGISTERED_COUNCIL));
  }

  @Test
  public void determineNextStep_whenHasPermissionIsNo_thenMainReason() {
    PermissionForm form = PermissionForm.builder().hasPermission(false).build();
    assertThat(form.determineNextStep(null)).isEqualTo(Optional.of(StepDefinition.PROVE_IDENTITY));
  }
}
