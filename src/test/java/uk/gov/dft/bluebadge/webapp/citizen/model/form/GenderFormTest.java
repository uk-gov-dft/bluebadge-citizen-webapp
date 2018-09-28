package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class GenderFormTest {

    @Test
    public void determineNextStep_ShowNinoIfApplicantIsNotYoung() {
        GenderForm form = GenderForm.builder().gender(GenderCodeField.FEMALE).build();
        Journey journey = new JourneyFixture.JourneyBuilder()
                .setDateOfBirth("1970", "05", "29").build();

        assertThat(form.determineNextStep(journey)).isNotEmpty();
        assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.NINO);
    }

    @Test
    public void determineNextStep_ShowNextStepIfApplicantIsYoung() {
        GenderForm form = GenderForm.builder().gender(GenderCodeField.FEMALE).build();
        Journey journey = new JourneyFixture.JourneyBuilder()
                .setDateOfBirth("2010", "05", "29").build();

        assertThat(form.determineNextStep(journey)).isNotEmpty();
        assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.HEALTH_CONDITIONS);
    }
}
