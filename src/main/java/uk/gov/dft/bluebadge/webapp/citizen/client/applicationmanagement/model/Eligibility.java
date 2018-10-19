package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

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
