package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

@Data
@Builder
public class SaveAndReturnForm implements BaseForm, Serializable {
  @Size(max = 100)
  @Pattern(regexp = ValidationPattern.EMAIL)
  private String emailAddress;
}
