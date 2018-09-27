package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
public class WalkingDifficultyForm implements StepForm {

  public enum WalkingDifficulty {
    HELP,
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
    if(WalkingDifficulty.NONE == walkingDifficulty){
      return Optional.of(StepDefinition.NOT_ELIGIBLE);
    }else{
      return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
    }
  }

  @NotNull
  private WalkingDifficulty walkingDifficulty;
}
