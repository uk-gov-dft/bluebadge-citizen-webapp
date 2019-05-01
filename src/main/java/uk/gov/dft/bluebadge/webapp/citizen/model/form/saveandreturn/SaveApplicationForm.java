package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveApplicationForm {
  @NotEmpty String emailAddress;
  @NotEmpty String postcode;
}
