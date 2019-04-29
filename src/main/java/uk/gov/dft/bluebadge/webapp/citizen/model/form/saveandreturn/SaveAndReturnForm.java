package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
public class SaveAndReturnForm implements Serializable {
  @NotEmpty String emailAddress;
}
