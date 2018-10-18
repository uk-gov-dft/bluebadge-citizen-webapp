package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Builder
@Data
public class WalkingDifficulty {
  
  private List<WalkingDifficultyTypeCodeField> typeCodes;
  private String otherDescription;
  private List<WalkingAid> walkingAids;
  private WalkingLengthOfTimeCodeField walkingLengthOfTimeCode;
  private WalkingSpeedCodeField walkingSpeedCode;
  private List<Treatment> treatments;
  private List<Medication> medications;
}
