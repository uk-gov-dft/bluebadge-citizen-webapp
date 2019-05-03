package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;

@Data
@Builder
public class SaveAndReturnForm implements Serializable {
  @Size(max = 100)
  @NotEmpty
  @Pattern(regexp = ValidationPattern.EMAIL)
  String emailAddress;
}
