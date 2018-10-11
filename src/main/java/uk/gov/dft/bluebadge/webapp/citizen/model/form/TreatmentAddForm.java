package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TreatmentAddForm implements Serializable {

  @Size(max = 100)
  @NotBlank
  private String treatmentDescription;

  @Size(max = 100)
  @NotBlank
  private String treatmentWhen;

  @Builder.Default
  private String id = UUID.randomUUID().toString();

}
