package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EnterCodeForm implements Serializable {
  @NotEmpty String postcode;
  @NotEmpty String code;
}
