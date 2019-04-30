package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class SaveApplicationForm {
  @NotEmpty
  String emailAddress;
  @NotEmpty
  String postcode;
}
