package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

@Data
public class MobilityAidAddForm implements Serializable {

  public enum AidType {
    WHEELCHAIR("Wheelchair"),
    SCOOTER("Mobility scooter"),
    WALKING_AID("Walking aid");

    private String type;

    AidType(String type) {

      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  @NotNull private AidType aidType;

  @Size(max = 100)
  @NotBlank
  private String usage;

  @NotNull private HowProvidedCodeField howProvidedCodeField;

  @Size(max = 100)
  private String customAidName;

  private String id;

  public MobilityAidAddForm() {
    id = UUID.randomUUID().toString();
  }

  public String getAidTypeDescription() {
    if (null == aidType) return "";
    if (aidType == AidType.WALKING_AID) return customAidName;
    return aidType.getType();
  }
}
