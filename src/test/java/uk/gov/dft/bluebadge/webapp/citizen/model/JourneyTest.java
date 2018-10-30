package uk.gov.dft.bluebadge.webapp.citizen.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

public class JourneyTest {

  @Test
  public void setFormForStep_cleansStepsChangeFromWalking() {
    Journey journey =
        new JourneyBuilder()
            .withEligibility(EligibilityCodeField.WALKD)
            .toStep(StepDefinition.DECLARATIONS)
            .build();

    // Forms exist before changing eligibility
    assertThat(journey.hasStepForm(StepDefinition.YOUR_ISSUING_AUTHORITY)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.CONTACT_DETAILS)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.WHERE_CAN_YOU_WALK)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.WALKING_DIFFICULTY)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.WALKING_TIME)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.MOBILITY_AID_LIST)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.TREATMENT_LIST)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.HEALTH_CONDITIONS)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.WHERE_CAN_YOU_WALK)).isTrue();

    journey.setFormForStep(
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.PIP).build());
    // After eligibility changed the eligibility forms have been removed
    // Other forms stay
    assertThat(journey.hasStepForm(StepDefinition.YOUR_ISSUING_AUTHORITY)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.CONTACT_DETAILS)).isTrue();
    assertThat(journey.hasStepForm(StepDefinition.WHERE_CAN_YOU_WALK)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.WALKING_DIFFICULTY)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.WALKING_TIME)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.MOBILITY_AID_LIST)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.TREATMENT_LIST)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.HEALTH_CONDITIONS)).isFalse();
    assertThat(journey.hasStepForm(StepDefinition.WHERE_CAN_YOU_WALK)).isFalse();
  }
}
