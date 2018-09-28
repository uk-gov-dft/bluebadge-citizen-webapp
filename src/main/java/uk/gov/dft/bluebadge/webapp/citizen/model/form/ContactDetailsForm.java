package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class ContactDetailsForm implements StepForm, Serializable {

  @NotBlank(message = "{field.fullName.NotBlank}")
  @Size(max = 100)
  private String fullName;

  @NotNull(message = "{field.hasBirthName.NotNull}")
  private Boolean hasBirthName;

  @Size(max = 100)
  private String birthName;

  public Boolean isBirthNameValid() {
    return (hasBirthName == null || !hasBirthName.equals(true) || !birthName.isEmpty());
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NAME;
  }
}
