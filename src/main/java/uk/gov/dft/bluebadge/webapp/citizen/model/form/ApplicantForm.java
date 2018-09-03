package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantForm implements Serializable {

  @NotNull(message = "{applicantType.NotNull}")
  private String applicantType;
}
