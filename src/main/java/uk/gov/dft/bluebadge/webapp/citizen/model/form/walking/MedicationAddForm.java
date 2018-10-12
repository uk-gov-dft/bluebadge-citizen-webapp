package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
public class MedicationAddForm implements Serializable {

  @Size(max = 100)
  @NotBlank
  private String name;

  // yes - no value with radio buttons..
  @NotNull
  private String prescribed;

  @Size(max = 100)
  @NotBlank
  private String dosage;

  private String id;

  public MedicationAddForm() {
    id = UUID.randomUUID().toString();
  }

}
