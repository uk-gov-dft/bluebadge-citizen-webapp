package uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.POSTCODE_CASE_INSENSITIVE;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

@Data
@Builder
public class EnterCodeForm implements BaseForm, Serializable {
  @NotEmpty
  @Pattern(regexp = POSTCODE_CASE_INSENSITIVE)
  String postcode;

  @NotEmpty String code;
}
