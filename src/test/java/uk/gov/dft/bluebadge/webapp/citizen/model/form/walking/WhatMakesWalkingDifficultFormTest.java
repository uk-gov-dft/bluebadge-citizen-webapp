package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class WhatMakesWalkingDifficultFormTest {
  private WhatMakesWalkingDifficultForm form;

  @Before
  public void setUp() {
    form = WhatMakesWalkingDifficultForm.builder().build();
  }

  @Test
  public void determineNextStep_whenBreathlessnessIsSelected_thenRedirectToBreathlessPage() {
    form.setWhatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.BREATH));
    assertThat(form.determineNextStep(null)).isEqualTo(Optional.of(StepDefinition.BREATHLESSNESS));
  }

  @Test
  public void
      determineNextStep_whenBreathlessnessIsNotSelected_thenRedirectToMobilityAidListPage() {
    form.setWhatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
    assertThat(form.determineNextStep(null))
        .isEqualTo(Optional.of(StepDefinition.MOBILITY_AID_LIST));
  }
}
