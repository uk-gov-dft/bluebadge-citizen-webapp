package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class SaveAndReturnJourney implements Serializable {
  @NotEmpty
  private SaveAndReturnForm saveAndReturnForm;
  @NotEmpty
  private EnterCodeForm enterCodeForm;
}
