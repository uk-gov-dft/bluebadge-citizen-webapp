package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WalkingDifficulty {

  private List<WalkingDifficultyTypeCodeField> typeCodes;
  private String painDescription;
  private String balanceDescription;
  private Boolean healthProfessionsForFalls;
  private String dangerousDescription;
  private Boolean chestLungHeartEpilepsy;
  private String otherDescription;
  private List<WalkingAid> walkingAids;
  private WalkingLengthOfTimeCodeField walkingLengthOfTimeCode;
  private WalkingSpeedCodeField walkingSpeedCode;
  private List<Treatment> treatments;
  private List<Medication> medications;
  private Breathlessness breathlessness;
}
