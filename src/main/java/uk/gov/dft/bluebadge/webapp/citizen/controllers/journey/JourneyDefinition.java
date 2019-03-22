package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task.*;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;

public enum JourneyDefinition {
  J_PRE_APPLICATION(CHECK_ELIGIBILITY),
  J_APPLY(DECLARATION, SUBMIT_AND_PAY),
  J_PIP(PERSONAL_DETAILS, PROOF_OF_BENEFIT, PROVE_ID, PROVE_ADDRESS, PROVIDE_PHOTO),
  J_DLA(PERSONAL_DETAILS, PROOF_OF_BENEFIT, PROVE_ID, PROVE_ADDRESS, PROVIDE_PHOTO),
  J_AFCS(PERSONAL_DETAILS, PROVE_ID, PROVE_ADDRESS, PROVIDE_PHOTO),
  J_WPMS(PERSONAL_DETAILS, PROVE_ID, PROVE_ADDRESS, PROVIDE_PHOTO),
  J_BLIND(PERSONAL_DETAILS, PROOF_OF_VISUAL, PROVE_ID, PROVE_ADDRESS, PROVIDE_PHOTO),
  J_ARMS(
      PERSONAL_DETAILS,
      DESC_CONDITION_ARMS,
      SUPPORTING_DOCS,
      PROVE_ID,
      PROVE_ADDRESS,
      PROVIDE_PHOTO),
  J_CHILDBULK(
      PERSONAL_DETAILS,
      DESC_CONDITION_CHILD,
      LIST_MEDICAL_EQUIP,
      LIST_HEALTH_PROFS,
      SUPPORTING_DOCS,
      PROVE_ID,
      PROVE_ADDRESS,
      PROVIDE_PHOTO),
  J_CHILDVEHIC(
      PERSONAL_DETAILS,
      DESC_CONDITION_CHILD,
      SUPPORTING_DOCS, // WHY in different order to above
      LIST_HEALTH_PROFS,
      PROVE_ID,
      PROVE_ADDRESS,
      PROVIDE_PHOTO),
  J_WALKD(
      PERSONAL_DETAILS,
      DESC_WALKING_ABILITY,
      SUPPORTING_DOCS, // WHY in different order to above
      LIST_HEALTH_PROFS,
      LIST_MEDICATION,
      LIST_TREATMENTS,
      PROVE_ID,
      PROVE_ADDRESS,
      PROVIDE_PHOTO);

  @Getter private List<Task> tasks;

  JourneyDefinition(Task... tasks) {
    this.tasks = ImmutableList.copyOf(tasks);
  }

  /**
   * Safe to assume that this can only return one Task. As a step can never be repeated within a
   * journey, nor a task
   */
  public Optional<Task> findTaskByStep(StepDefinition stepDefinition) {
    return getTasks().stream().filter(t -> t.getSteps().contains(stepDefinition)).findFirst();
  }

  public static JourneyDefinition findForEligibilityCode(EligibilityCodeField eligibilityCode) {
    if (null == eligibilityCode) {
      return null;
    }

    switch (eligibilityCode) {
      case PIP:
        return J_PIP;
      case DLA:
        return J_DLA;
      case AFRFCS:
        return J_AFCS;
      case WPMS:
        return J_WPMS;
      case BLIND:
        return J_BLIND;
      case WALKD:
        return J_WALKD;
      case ARMS:
        return J_ARMS;
      case CHILDBULK:
        return J_CHILDBULK;
      case CHILDVEHIC:
        return J_CHILDVEHIC;
      default:
        return null;
    }
  }
}
