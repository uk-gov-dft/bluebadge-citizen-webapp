package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

@Data
@EqualsAndHashCode
public class MobilityAidAddForm implements Serializable {

  @NotBlank
  @Size(max = 100)
  private String aidType;

  @Size(max = 100)
  @NotBlank
  private String usage;

  @NotNull private HowProvidedCodeField howProvidedCodeField;

  // Validation done in controller, not bean as is conditional (and was easier).
  private String customAidName;

  private String id = UUID.randomUUID().toString();
}
