package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthConditionsForm implements Serializable {
  @NotNull
  @Size(min = 1, max = 100)
  String descriptionOfConditions;
}
