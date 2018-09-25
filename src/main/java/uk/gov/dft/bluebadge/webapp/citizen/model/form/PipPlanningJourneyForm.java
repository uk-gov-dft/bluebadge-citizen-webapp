package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_12;

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
    LocalAuthorityRefData localAuthority = journey.getLocalAuthority();
    if (PLANNING_POINTS_12.equals(planningJourneyOption)) {
      return Optional.of(StepDefinition.ELIGIBLE);
    }
    if (Nations.WALES.equals(localAuthority.getNation())) {
      return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
    }
    if (Nations.SCOTLAND.equals(localAuthority.getNation())) {
      return Optional.of(StepDefinition.PIP_DLA);
    }
    log.error("PipPlanningJourneyForm: could not determine next step. LA={}", localAuthority);
    return Optional.empty();
  }

  @NotNull private PipPlanningJourneyForm.PipPlanningJourneyOption planningJourneyOption;
}
