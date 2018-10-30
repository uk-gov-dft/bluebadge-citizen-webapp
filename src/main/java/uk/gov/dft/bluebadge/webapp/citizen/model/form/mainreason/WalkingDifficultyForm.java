package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class WalkingDifficultyForm implements StepForm, Serializable {

  public enum WalkingDifficulty {
    HELP,
    PLAN,
    PAIN,
    DANGEROUS,
    NONE
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WALKING_DIFFICULTY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (WalkingDifficulty.NONE == walkingDifficulty) {
      return Optional.of(StepDefinition.NOT_ELIGIBLE);
    } else {
      return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  @NotNull private WalkingDifficulty walkingDifficulty;
}
