package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthConditionsForm {
  @NotNull
  @Size(min = 1, max = 100)
  String descriptionOfConditions;
}
