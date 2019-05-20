package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SaveAndReturnJourney implements Serializable {
  public static final String SAVE_AND_RETURN_JOURNEY_KEY = "saveAndReturnJourney";
  @NotEmpty private SaveAndReturnForm saveAndReturnForm;
  @NotEmpty private EnterCodeForm enterCodeForm;
}
