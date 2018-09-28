package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class ContactDetailsForm implements StepForm, Serializable {

  @Size(max = 100)
  private String fullName;

  @Size(max = 100)
  @NotNull
  private String primaryPhoneNumber;

  @Size(max = 100)
  private String secondaryPhoneNumber;

  @Size(max = 100)
  private String emailAddress;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CONTACT_DETAILS;
  }

  public boolean isFullnameInvalid(Journey journey) {
    return !journey.isApplicantYourself() && StringUtils.isBlank(fullName);
  }
}
