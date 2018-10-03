package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

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
  @NotBlank private String usage;
  @NotNull private HowProvidedCodeField howProvidedCodeField;
  private String id;

  public MobilityAidAddForm() {
    id = UUID.randomUUID().toString();
  }

  public String getAidTypeDescription() {
    if (null == aidType) return "";

    return aidType.getType();
  }
}
