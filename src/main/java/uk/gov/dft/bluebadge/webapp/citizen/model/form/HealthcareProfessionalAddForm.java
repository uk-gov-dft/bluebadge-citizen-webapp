package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class HealthcareProfessionalAddForm implements Serializable {

  @Size(max = 100)
  @NotBlank
  private String healthcareProfessionalName;

  @Size(max = 100)
  @NotBlank
  private String healthcareProfessionalLocation;

  private String id = UUID.randomUUID().toString();
}
