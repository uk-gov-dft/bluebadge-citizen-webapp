package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveAndReturnForm implements Serializable {
  @Size(max = 100, message = "Size.emailAddress")
  @NotEmpty(message = "NotEmpty.emailAddress")
  String emailAddress;
}
