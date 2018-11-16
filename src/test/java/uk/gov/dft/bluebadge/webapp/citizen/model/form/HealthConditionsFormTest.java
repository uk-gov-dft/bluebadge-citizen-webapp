package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.util.EnumSet;
import java.util.Optional;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

public class HealthConditionsFormTest {

  @Test
  public void givenArms_thenNextStepIsHowOftenDrive() {
    Journey journey = new Journey();
    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(ARMS).build());

    HealthConditionsForm form = HealthConditionsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.ARMS_HOW_OFTEN_DRIVE);
  }

  @Test
  public void givenWalking_thenNextStepIsWalkingDifficulties() {
    Journey journey = new Journey();
    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(WALKD).build());

    HealthConditionsForm form = HealthConditionsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.WHAT_WALKING_DIFFICULTIES);
  }

  @Test
  public void givenChildBorV_thenNextStepIsDeclaration() {
    Journey journey = new Journey();

    EnumSet<EligibilityCodeField> childPaths = EnumSet.of(CHILDBULK, CHILDVEHIC);
    assertThat(childPaths).isNotEmpty();
    for (EligibilityCodeField eligibility : childPaths) {

      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(eligibility).build());

      HealthConditionsForm form = HealthConditionsForm.builder().build();
      Optional<StepDefinition> nextStep = form.determineNextStep(journey);

      assertThat(nextStep).isNotEmpty();
      assertThat(nextStep.get()).isEqualTo(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
    }
  }

  @Test
  public void givenOtherCondition_thenNextStepIsDeclaration() {
    Journey journey = new Journey();

    EnumSet<EligibilityCodeField> others =
        EnumSet.complementOf(EnumSet.of(ARMS, WALKD, CHILDBULK, CHILDVEHIC));
    assertThat(others).isNotEmpty();
    for (EligibilityCodeField eligibility : others) {

      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(eligibility).build());

      HealthConditionsForm form = HealthConditionsForm.builder().build();
      Optional<StepDefinition> nextStep = form.determineNextStep(journey);

      assertThat(nextStep).isNotEmpty();
      assertThat(nextStep.get()).isEqualTo(StepDefinition.DECLARATIONS);
    }
  }
}
