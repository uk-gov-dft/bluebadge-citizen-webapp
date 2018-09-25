package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nations;
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
    LocalAuthorityRefData localAuthority = journey.getLocalAuthority();
    switch (movingAroundPoints) {
      case MOVING_POINTS_8:
      case MOVING_POINTS_10:
      case MOVING_POINTS_12:
        return Optional.of(StepDefinition.ELIGIBLE);
      case MOVING_POINTS_0:
      case MOVING_POINTS_4:
        if (Nations.ENGLAND.equals(localAuthority.getNation())) {
          return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
        }
        if (Nations.SCOTLAND.equals(localAuthority.getNation())
            || Nations.WALES.equals(localAuthority.getNation())) {
          return Optional.of(StepDefinition.PIP_PLANNING_JOURNEY);
        }
        // TODO Northern Ireland
        log.error(
            "Invalid nation in local authority ref data {}:{}",
            localAuthority.getShortCode(),
            localAuthority.getDescription());
        return Optional.empty();
    }
    return Optional.empty();
  }

  @NotNull private PipMovingAroundOption movingAroundPoints;
}
