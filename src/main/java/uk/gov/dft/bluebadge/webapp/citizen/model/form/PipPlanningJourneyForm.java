package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_0;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_10;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_12;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_4;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_8;

@Data
@Builder
public class PipPlanningJourneyForm implements Serializable, StepForm {

  public enum PipPlanningJourneyOption {
    PLANNING_POINTS_12,
    PLANNING_POINTS_10,
    PLANNING_POINTS_8,
    PLANNING_POINTS_4,
    PLANNING_POINTS_0
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    // TODO
    return Optional.of(StepDefinition.ELIGIBLE);
  }

  private PipMovingAroundForm.PipMovingAroundOption movingAroundPoints;

}
