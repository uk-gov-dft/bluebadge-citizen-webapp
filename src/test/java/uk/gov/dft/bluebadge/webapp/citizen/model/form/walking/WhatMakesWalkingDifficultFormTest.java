package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WhatMakesWalkingDifficultFormTest {
    private WhatMakesWalkingDifficultForm form;

    @Before
    public void setUp() {
        form = WhatMakesWalkingDifficultForm.builder().build();
    }

    @Test
    public void
    determineNextStep_whenBreathlessnessIsSelected_thenRedirectToBreathlessPage() {
        form.setWhatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.BREATH));
        assertThat(form.determineNextStep())
                .isEqualTo(Optional.of(StepDefinition.BREATHLESSNESS));
    }

    @Test
    public void
    determineNextStep_whenBreathlessnessIsNotSelected_thenRedirectToMobilityAidListPage() {
        form.setWhatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN));
        assertThat(form.determineNextStep())
                .isEqualTo(Optional.of(StepDefinition.MOBILITY_AID_LIST));
    }

    @Test
    public void getValidationGroups_shouldReturnValidationSequenceWithSomethingElseDescription_whenDifficultyTypeIsSomethingElse() {
        WhatMakesWalkingDifficultForm.GroupProvider groupProvider = new WhatMakesWalkingDifficultForm.GroupProvider();
        List<Class<?>> validationSequence = groupProvider.getValidationGroups(JourneyFixture.getWhatMakesWalkingDifficultForm());
        assertThat(validationSequence).isEqualTo(
                Lists.newArrayList(WhatMakesWalkingDifficultForm.class, WhatMakesWalkingDifficultForm.SomethingElseGroup.class));
    }

    @Test
    public void getValidationGroups_shouldReturnValidationSequenceWithoutSomethingElseDescription_whenDifficultyTypeIsNotSomethingElse() {
        WhatMakesWalkingDifficultForm.GroupProvider groupProvider = new WhatMakesWalkingDifficultForm.GroupProvider();
        List<Class<?>> validationSequence = groupProvider.getValidationGroups(JourneyFixture.getWhatMakesWalkingDifficultFormWithoutSomethingElse());
        assertThat(validationSequence).isEqualTo(
                Lists.newArrayList(WhatMakesWalkingDifficultForm.class));
    }
}