package uk.gov.dft.bluebadge.webapp.citizen.model.form.pip;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_12;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@Slf4j
@EqualsAndHashCode
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
    return StepDefinition.PIP_PLANNING_JOURNEY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {

    if (PLANNING_POINTS_12.equals(planningJourneyOption)) {
      return Optional.of(StepDefinition.ELIGIBLE);
    }
    if (Nation.WLS.equals(journey.getNation())) {
      return Optional.of(StepDefinition.MAIN_REASON);
    }
    if (Nation.SCO.equals(journey.getNation())) {
      return Optional.of(StepDefinition.PIP_DLA);
    }
    log.error(
        "PipPlanningJourneyForm: could not determine next step. LA={}",
        journey.getLocalAuthority());
    return Optional.empty();
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  @NotNull private PipPlanningJourneyForm.PipPlanningJourneyOption planningJourneyOption;
}
