package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
@GroupSequenceProvider(WhatMakesWalkingDifficultForm.GroupProvider.class)
public class WhatMakesWalkingDifficultForm implements StepForm, Serializable {
  @NotEmpty List<WalkingDifficultyTypeCodeField> whatWalkingDifficulties;

  @NotBlank(groups = SomethingElseGroup.class)
  @Size(max = 255, groups = SomethingElseGroup.class)
  String somethingElseDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WHAT_WALKING_DIFFICULTIES;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  public static class GroupProvider
      implements DefaultGroupSequenceProvider<WhatMakesWalkingDifficultForm> {
    @Override
    public List<Class<?>> getValidationGroups(WhatMakesWalkingDifficultForm object) {
      List<Class<?>> sequence = new ArrayList<>();
      sequence.add(WhatMakesWalkingDifficultForm.class);
      if (null != object
          && null != object.getWhatWalkingDifficulties()
          && object.getWhatWalkingDifficulties().contains(WalkingDifficultyTypeCodeField.SOMELSE)) {
        sequence.add(SomethingElseGroup.class);
      }
      return sequence;
    }
  }

  interface SomethingElseGroup {}
}
