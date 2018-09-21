package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantNameForm implements Serializable {

  @NotBlank(message = "{field.fullName.NotBlank}")
  @Size(max = 100)
  private String fullName;

  @NotNull(message = "{field.hasBirthName.NotNull}")
  private Boolean hasBirthName;

  @Size(max = 100)
  private String birthName;

  public Boolean isBirthNameValid() {
    return (hasBirthName == null || !hasBirthName.equals(true) || !birthName.isEmpty());
  }
}
