package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;

@Data
@EqualsAndHashCode
public class TreatmentAddForm implements BaseForm, Serializable {
  @NotNull(message = "{NotNull.treatment.whenTypeCodeField}")
  private String whenTypeCodeField;

  @Size(max = 100)
  @NotBlank
  private String treatmentDescription;

  @Size(max = 100)
  private String treatmentWhen;

  @Size(max = 100)
  private String treatmentDetailField;

  @Size(max = 100)
  private String treatmentOptionalDetailField;

  private String id = UUID.randomUUID().toString();

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("treatmentDescription", "treatmentWhen");
  }
}
