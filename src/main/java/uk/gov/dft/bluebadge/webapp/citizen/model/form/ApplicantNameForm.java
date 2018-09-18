package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantNameForm implements Serializable {

  @NotBlank(message = "{field.fullName.NotBlank}")
  private String fullName;

  @NotNull(message = "Select whether birth name has changed")
  private Boolean hasBirthName;

  private String birthName;

  public Boolean isBirthNameValid() {
    if (hasBirthName != null && hasBirthName.equals(true) && birthName.isEmpty()) {
      return false;
    }
    return true;
  }
}
