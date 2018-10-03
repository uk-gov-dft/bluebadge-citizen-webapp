package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class WalkingDifficultyFormTest {

  @Test
  public void determineNextStep_whenNotNone_thenMayBeEligible() {
    EnumSet.of(
            WalkingDifficultyForm.WalkingDifficulty.DANGEROUS,
            WalkingDifficultyForm.WalkingDifficulty.PAIN,
            WalkingDifficultyForm.WalkingDifficulty.HELP)
        .forEach(
            e -> {
              WalkingDifficultyForm form =
                  WalkingDifficultyForm.builder().walkingDifficulty(e).build();
              assertThat(form.determineNextStep()).isNotEmpty();
              assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
            });
  }

  @Test
  public void determineNextStep_whenNone_thenNotEligible() {
    WalkingDifficultyForm form =
        WalkingDifficultyForm.builder()
            .walkingDifficulty(WalkingDifficultyForm.WalkingDifficulty.NONE)
            .build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.NOT_ELIGIBLE);
  }
}
