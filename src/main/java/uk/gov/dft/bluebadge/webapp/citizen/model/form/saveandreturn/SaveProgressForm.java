package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.POSTCODE_CASE_INSENSITIVE;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

@Data
@Builder
public class SaveProgressForm implements BaseForm, Serializable {
  @Size(max = 100)
  @Pattern(regexp = ValidationPattern.EMAIL)
  private String emailAddress;

  @Pattern(regexp = POSTCODE_CASE_INSENSITIVE)
  private String postcode;
}
