package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.Optional;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

public class HealthConditionsFormTest {
  @Test
  public void givenWalking_thenNextStepIsWalkingDifficulties() {
    Journey journey = new Journey();
    journey.setMainReasonForm(
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.WALKD).build());

    HealthConditionsForm form = HealthConditionsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.WHAT_WALKING_DIFFICULTIES);
  }

  @Test
  public void givenOtherCondition_thenNextStepIsDeclaration() {
    Journey journey = new Journey();

    EnumSet<EligibilityCodeField> others =
        EnumSet.complementOf(EnumSet.of(EligibilityCodeField.WALKD));
    assertThat(others).isNotEmpty();
    for (EligibilityCodeField eligibility : others) {

      journey.setMainReasonForm(MainReasonForm.builder().mainReasonOption(eligibility).build());

      HealthConditionsForm form = HealthConditionsForm.builder().build();
      Optional<StepDefinition> nextStep = form.determineNextStep(journey);

      assertThat(nextStep).isNotEmpty();
      assertThat(nextStep.get()).isEqualTo(StepDefinition.DECLARATIONS);
    }
  }
}
