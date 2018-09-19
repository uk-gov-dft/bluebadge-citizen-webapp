package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ApplicantNameForm implements StepForm, Serializable {

  @NotBlank(message = "{field.fullName.NotBlank}")
  private String fullName;

  @NotNull(message = "Select whether birth name has changed")
  private Boolean hasBirthName;

  private String birthName;

  public Boolean isBirthNameValid() {
    if (hasBirthName != null && hasBirthName.equals(true) && birthName.isEmpty()) {
      return false;
    }
    return true;
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NAME;
  }
}
