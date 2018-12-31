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

public class UploadSupportingDocumentsFormTest {

  @Test
  public void givenWalking_thenNextStepIsTreatmentList() {
    Journey journey = new Journey();
    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(WALKD).build());

    UploadSupportingDocumentsForm form = UploadSupportingDocumentsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.TREATMENT_LIST);
  }

  @Test
  public void givenArms_thenNextStepIsHowOftenDrive() {
    Journey journey = new Journey();
    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(ARMS).build());

    UploadSupportingDocumentsForm form = UploadSupportingDocumentsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.ARMS_HOW_OFTEN_DRIVE);
  }

  @Test
  public void givenChildBulk_thenNextStepIsMedicalEquipment() {
    Journey journey = new Journey();

    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(CHILDBULK).build());

    UploadSupportingDocumentsForm form = UploadSupportingDocumentsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.MEDICAL_EQUIPMENT);
  }

  @Test
  public void givenChildVehic_thenNextStepIsDeclaration() {
    Journey journey = new Journey();

    journey.setFormForStep(MainReasonForm.builder().mainReasonOption(CHILDVEHIC).build());

    UploadSupportingDocumentsForm form = UploadSupportingDocumentsForm.builder().build();
    Optional<StepDefinition> nextStep = form.determineNextStep(journey);

    assertThat(nextStep).isNotEmpty();
    assertThat(nextStep.get()).isEqualTo(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
  }

  @Test
  public void givenOtherCondition_thenThrowIllegalStateException() throws Exception {
    Journey journey = new Journey();

    EnumSet<EligibilityCodeField> others =
        EnumSet.complementOf(EnumSet.of(ARMS, WALKD, CHILDBULK, CHILDVEHIC));
    assertThat(others).isNotEmpty();
    for (EligibilityCodeField eligibility : others) {

      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(eligibility).build());

      UploadSupportingDocumentsForm form = UploadSupportingDocumentsForm.builder().build();

      try {
        Optional<StepDefinition> nextStep = form.determineNextStep(journey);
        throw new Exception(
            "Should have thrown an IllegalStateException for eligibility: " + eligibility);
      } catch (IllegalStateException ex) {
        assertThat(ex).hasMessageStartingWith("Failed to determine next step for current step:");
      }
    }
  }
}
