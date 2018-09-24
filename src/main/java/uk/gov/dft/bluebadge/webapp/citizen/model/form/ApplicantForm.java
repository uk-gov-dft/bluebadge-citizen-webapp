package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ApplicantForm implements StepForm, Serializable {

  @NotNull(message = "{applicantType.NotNull}")
  private String applicantType;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.APPLICANT_TYPE;
  }
}
