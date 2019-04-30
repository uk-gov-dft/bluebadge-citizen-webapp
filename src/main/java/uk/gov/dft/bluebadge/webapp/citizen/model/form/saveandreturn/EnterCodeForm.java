package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class EnterCodeForm implements Serializable {
  @NotEmpty
  String postcode;
  @NotEmpty
  String code;
}
