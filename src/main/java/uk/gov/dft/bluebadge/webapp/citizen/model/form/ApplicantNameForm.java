package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ApplicantNameForm implements StepForm, Serializable {

  @Size(max = 100)
  private String fullName;

  @Size(max = 100)
  @NotNull
  private String primaryContactNumber;

  @Size(max = 100)
  private String secondaryContactNumber;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CONTACT_DETAILS;
  }
}
