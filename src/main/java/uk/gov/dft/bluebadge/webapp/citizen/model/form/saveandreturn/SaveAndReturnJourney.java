package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SaveAndReturnJourney implements Serializable {
  @NotEmpty private SaveAndReturnForm saveAndReturnForm;
  @NotEmpty private EnterCodeForm enterCodeForm;
}
