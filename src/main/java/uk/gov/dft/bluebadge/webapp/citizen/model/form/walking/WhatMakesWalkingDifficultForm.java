package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class WhatMakesWalkingDifficultForm implements StepForm, Serializable {
  @NotEmpty List<WalkingDifficultyTypeCodeField> whatWalkingDifficulties;

  private String painDescription;
  private String balanceDescription;
  private Boolean healthProfessionsForFalls;
  private String dangerousDescription;
  private Boolean chestLungHeartEpilepsy;
  String somethingElseDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WHAT_MAKES_WALKING_DIFFICULT;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (whatWalkingDifficulties.contains(WalkingDifficultyTypeCodeField.BREATH)) {
      return Optional.of(StepDefinition.BREATHLESSNESS);
    }
    return Optional.of(StepDefinition.MOBILITY_AID_LIST);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
