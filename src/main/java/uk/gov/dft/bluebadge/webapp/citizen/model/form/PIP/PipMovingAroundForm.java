package uk.gov.dft.bluebadge.webapp.citizen.model.form.PIP;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@Slf4j
public class PipMovingAroundForm implements Serializable, StepForm {
  public enum PipMovingAroundOption {
    MOVING_POINTS_12,
    MOVING_POINTS_10,
    MOVING_POINTS_8,
    MOVING_POINTS_4,
    MOVING_POINTS_0
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    switch (movingAroundPoints) {
      case MOVING_POINTS_8:
      case MOVING_POINTS_10:
      case MOVING_POINTS_12:
        return Optional.of(StepDefinition.ELIGIBLE);
      case MOVING_POINTS_0:
      case MOVING_POINTS_4:
        if (Nation.ENG.equals(journey.getNation())) {
          return Optional.of(StepDefinition.MAIN_REASON);
        }
        if (Nation.SCO.equals(journey.getNation())
            || Nation.WLS.equals(journey.getNation())) {
          return Optional.of(StepDefinition.PIP_PLANNING_JOURNEY);
        }
        log.error(
            "Invalid nation in local authority ref data {}:{}",
            journey.getLocalAuthority().getShortCode(),
            journey.getLocalAuthority().getDescription());
        return Optional.empty();
    }
    return Optional.empty();
  }

  @NotNull private PipMovingAroundOption movingAroundPoints;
}
