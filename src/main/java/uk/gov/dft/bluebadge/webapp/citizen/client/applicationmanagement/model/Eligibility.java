package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Eligibility {

  private EligibilityCodeField typeCode;
  private String descriptionOfConditions;
  private Benefit benefit;
  private WalkingDifficulty walkingDifficulty;
  private DisabilityArms disabilityArms;
  private List<HealthcareProfessional> healthcareProfessionals;
  private Blind blind;
  private ChildUnder3 childUnder3;
}
