package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class ApplicantNameForm implements StepForm, Serializable {
  @NotBlank(message = "{field.fullName.NotBlank}")
  @Pattern(regexp = "^[\\p{L} \\.'\\-]*$")
  @Size(max = 100)
  private String fullName;

  @NotNull(message = "{field.hasBirthName.NotNull}")
  private Boolean hasBirthName;

  @Size(max = 100)
  @Pattern(regexp = "^[\\p{L} \\.'\\-]*$")
  private String birthName;

  public Boolean isBirthNameValid() {
    return (!Boolean.TRUE.equals(hasBirthName) || StringUtils.isNotBlank(birthName));
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NAME;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
