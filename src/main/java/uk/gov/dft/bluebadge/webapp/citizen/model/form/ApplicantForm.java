package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantForm implements Serializable {

  @NotBlank(message = "{applicantType.NotBlank}")
  private String applicantType;
}
