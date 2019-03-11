package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import com.google.common.collect.Lists;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BreathlessnessFormTest {
    @Test
    public void getValidationGroups_shouldReturnValidationSequenceWithOtherDescription_whenBreathlessTypeIsOther() {
        BreathlessnessForm.GroupProvider groupProvider = new BreathlessnessForm.GroupProvider();
        List<Class<?>> validationSequence = groupProvider.getValidationGroups(JourneyFixture.getBreathlessnessForm());
        assertThat(validationSequence).isEqualTo(
                Lists.newArrayList(BreathlessnessForm.class, BreathlessnessForm.OtherGroup.class));
    }

    @Test
    public void getValidationGroups_shouldReturnValidationSequenceWithOtherDescription_whenBreathlessTypeIsNotOther() {
        BreathlessnessForm.GroupProvider groupProvider = new BreathlessnessForm.GroupProvider();
        List<Class<?>> validationSequence = groupProvider.getValidationGroups(JourneyFixture.getBreathlessnessFormWithoutOther());
        assertThat(validationSequence).isEqualTo(
                Lists.newArrayList(BreathlessnessForm.class));
    }
}