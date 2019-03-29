package uk.gov.dft.bluebadge.webapp.citizen.config;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.*;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneySection;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneySpecification;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.BlindTask;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.CheckEligibilityTask;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.ProveAddressTask;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.SimpleTask;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.SubmitAndPayTask;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.WalkingTask;

@Configuration
public class JourneyTaskConfig {

  @Bean
  public JourneySpecification journeyBuilder() {

    ImmutableMap.Builder<EligibilityCodeField, JourneySection> map = ImmutableMap.builder();
    map.put(EligibilityCodeField.PIP, pipSection());
    map.put(EligibilityCodeField.DLA, dlaSection());
    map.put(EligibilityCodeField.AFRFCS, afcsSection());
    map.put(EligibilityCodeField.WPMS, wpmsSection());
    map.put(EligibilityCodeField.BLIND, blindSection());
    map.put(EligibilityCodeField.ARMS, armsSection());
    map.put(EligibilityCodeField.CHILDBULK, childbulkSection());
    map.put(EligibilityCodeField.CHILDVEHIC, childvehicSection());
    map.put(EligibilityCodeField.WALKD, walkdSection());

    return JourneySpecification.builder()
        .preApplicationJourney(preApplicationSection())
        .submitAndPayJourney(applySection())
        .eligibilityCodeToJourneyMap(map.build())
        .build();
  }

  private JourneySection preApplicationSection() {
    return new JourneySection("Pre Application", eligibilityTask());
  }

  private JourneySection applySection() {
    return new JourneySection("Apply", declaration(), submitAndPay());
  }

  private JourneySection pipSection() {
    return new JourneySection(
        "PIP", personalDetails(), proofOfBenefit(), proveId(), proveAddress(), providePhoto());
  }

  private JourneySection dlaSection() {
    return new JourneySection(
        "DLA", personalDetails(), proofOfBenefit(), proveId(), proveAddress(), providePhoto());
  }

  private JourneySection afcsSection() {
    return new JourneySection("AFCS", personalDetails(), proveId(), proveAddress(), providePhoto());
  }

  private JourneySection wpmsSection() {
    return new JourneySection("WPMS", personalDetails(), proveId(), proveAddress(), providePhoto());
  }

  private JourneySection blindSection() {
    return new JourneySection(
        "BLIND", personalDetails(), proofOfVisual(), proveId(), proveAddress(), providePhoto());
  }

  private JourneySection armsSection() {
    return new JourneySection(
        "Arms",
        personalDetails(),
        descConditionArms(),
        supportingDocs(),
        proveId(),
        proveAddress(),
        providePhoto());
  }

  private JourneySection childbulkSection() {
    return new JourneySection(
        "Child Bulk",
        personalDetails(),
        descConditionChild(),
        supportingDocs(),
        listMedicalEquip(),
        listHealthProfs(),
        proveId(),
        proveAddress(),
        providePhoto());
  }

  private JourneySection childvehicSection() {
    return new JourneySection(
        "Child Vehicle",
        personalDetails(),
        descConditionChild(),
        supportingDocs(),
        listHealthProfs(),
        proveId(),
        proveAddress(),
        providePhoto());
  }

  private JourneySection walkdSection() {
    return new JourneySection(
        "Walking",
        personalDetails(),
        descWalkingAbility(),
        supportingDocs(),
        listMedication(),
        listTreatments(),
        listHealthProfs(),
        proveId(),
        proveAddress(),
        providePhoto());
  }

  private Task eligibilityTask() {
    return new CheckEligibilityTask(
        "TODO", // TODO title
        HOME,
        APPLICANT_TYPE,
        FIND_COUNCIL,
        CHOOSE_COUNCIL,
        DIFFERENT_SERVICE_SIGNPOST,
        YOUR_ISSUING_AUTHORITY,
        EXISTING_BADGE,
        RECEIVE_BENEFITS,
        ELIGIBLE,
        MAY_BE_ELIGIBLE,
        NOT_ELIGIBLE,

        // Organisation journey
        ORGANISATION_NOT_ELIGIBLE,
        ORGANISATION_MAY_BE_ELIGIBLE,
        ORGANISATION_TRANSPORT,
        ORGANISATION_CARE,

        // Main reason journey
        WALKING_DIFFICULTY,
        CONTACT_COUNCIL,
        MAIN_REASON,

        // DLA Journey
        HIGHER_RATE_MOBILITY,

        // PIP Journey
        PIP_DLA,
        PIP_PLANNING_JOURNEY,
        PIP_MOVING_AROUND,

        // AFCS Journey
        AFCS_MENTAL_DISORDER,
        AFCS_DISABILITY,
        AFCS_COMPENSATION_SCHEME);
  }

  private Task proveAddress() {
    return new ProveAddressTask("taskList.application.section.proveAddress", PROVE_ADDRESS);
  }

  private Task personalDetails() {
    return new SimpleTask(
        "taskList.application.section.personalDetails", NAME, DOB, GENDER, NINO, ADDRESS, CONTACT_DETAILS);
  }

  private Task proofOfBenefit() {
    return new SimpleTask("taskList.application.section.provideProofBenefit", PROVE_BENEFIT, UPLOAD_BENEFIT);
  }

  private Task proveId() {
    return new SimpleTask("taskList.application.section.proveIdentity", PROVE_IDENTITY);
  }

  private Task providePhoto() {
    return new SimpleTask("taskList.application.section.providePhoto", StepDefinition.PROVIDE_PHOTO);
  }

  private Task proofOfVisual() {
    return new BlindTask(
        "taskList.application.section.provideProofVisual", REGISTERED, PERMISSION, REGISTERED_COUNCIL);
  }

  private Task descConditionArms() {
    return new SimpleTask(
        "taskList.application.section.descCondition",
        HEALTH_CONDITIONS,
        ARMS_HOW_OFTEN_DRIVE,
        ARMS_DRIVE_ADAPTED_VEHICLE,
        ARMS_DIFFICULTY_PARKING_METER);
  }

  private Task descConditionChild() {
    return new SimpleTask("taskList.application.section.descCondition", HEALTH_CONDITIONS);
  }

  private Task descWalkingAbility() {
    return new WalkingTask(
        "taskList.application.section.descWalkingAbility",
        HEALTH_CONDITIONS,
        WHAT_MAKES_WALKING_DIFFICULT,
        BREATHLESSNESS,
        MOBILITY_AID_LIST,
        WALKING_TIME,
        WHERE_CAN_YOU_WALK);
  }

  private Task supportingDocs() {
    return new SimpleTask("taskList.application.section.supportingDocs", UPLOAD_SUPPORTING_DOCUMENTS);
  }

  private Task listMedicalEquip() {
    return new SimpleTask("taskList.application.section.medicalEquip", MEDICAL_EQUIPMENT);
  }

  private Task listHealthProfs() {
    return new SimpleTask("taskList.application.section.healthcaseProfs", HEALTHCARE_PROFESSIONAL_LIST);
  }

  private Task listMedication() {
    return new SimpleTask("taskList.application.section.medication", MEDICATION_LIST);
  }

  private Task listTreatments() {
    return new SimpleTask("taskList.application.section.treatments", TREATMENT_LIST);
  }

  private Task declaration() {
    return new SimpleTask("taskList.apply.section.declaration", DECLARATIONS);
  }

  private Task submitAndPay() {
    return new SubmitAndPayTask(
        "taskList.apply.section.submitAndPay", // TODO Different if payments enabled/not
        BADGE_PAYMENT,
        NOT_PAID,
        BADGE_PAYMENT_RETURN,
        SUBMITTED);
  }
}
