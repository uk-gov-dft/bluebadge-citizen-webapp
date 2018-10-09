package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
public class TreatmentAddForm implements Serializable {

  @Size(max = 100)
  @NotBlank
  private String treatmentDescription;

  @Size(max = 100)
  @NotBlank
  private String treatmentWhen;

  private String id;

  public TreatmentAddForm() {
    id = UUID.randomUUID().toString();
  }
}
