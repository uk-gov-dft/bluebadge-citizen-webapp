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
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    // TODO
    return Optional.of(StepDefinition.ELIGIBLE);
  }

  public enum PipPlanningJourneyOption implements Option {
    PLANNING_POINTS_12("pip.planning.journey.12"),
    PLANNING_POINTS_10("pip.planning.journey.12"),
    PLANNING_POINTS_8("pip.planning.journey.12"),
    PLANNING_POINTS_4("pip.planning.journey.12"),
    PLANNING_POINTS_0("pip.planning.journey.12");

    private String messageKey;

    PipPlanningJourneyOption(String messageKey) {

      this.messageKey = messageKey;
    }

    @Override
    public String getShortCode() {
      return this.name();
    }

    @Override
    public String getMessageKey() {
      return messageKey;
    }
  }

  private PipMovingAroundForm.PipMovingAroundOption movingAroundPoints;

  public static final List<Option> options =
      Lists.newArrayList(
          PLANNING_POINTS_12,
          PLANNING_POINTS_10,
          PLANNING_POINTS_8,
          PLANNING_POINTS_4,
          PLANNING_POINTS_0);
}
