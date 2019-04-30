package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class SaveAndReturnForm implements Serializable {
  @Size(max = 100, message = "Size.emailAddress")
  @NotEmpty(message = "NotEmpty.emailAddress")
  String emailAddress;
}
