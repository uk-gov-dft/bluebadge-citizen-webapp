package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TreatmentAddForm implements BaseForm, Serializable {

  @Size(max = 100)
  @NotBlank
  private String treatmentDescription;

  @Size(max = 100)
  @NotBlank
  private String treatmentWhen;

  private String id = UUID.randomUUID().toString();

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("treatmentDescription", "treatmentWhen");
  }
}
