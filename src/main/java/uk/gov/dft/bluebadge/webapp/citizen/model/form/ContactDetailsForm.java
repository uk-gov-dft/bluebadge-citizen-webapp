package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class ContactDetailsForm implements StepForm, Serializable {

  private static final String PHONE_NUMBER_REGEX = "^$|^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$";
  private static final String EMAIL_REGEX = "^$|.+\\@.+";
  @Size(max = 100)
  private String fullName;

  @Size(max = 100)
  @NotBlank
  @Pattern(regexp = PHONE_NUMBER_REGEX, message = "{Invalid.primaryPhoneNumber}")
  private String primaryPhoneNumber;

  @Size(max = 100)
  @Pattern(regexp = PHONE_NUMBER_REGEX, message = "{Invalid.secondaryPhoneNumber}")
  private String secondaryPhoneNumber;

  @Size(max = 100)
  @Pattern(regexp = EMAIL_REGEX, message = "{Invalid.emailAddress}")
  private String emailAddress;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CONTACT_DETAILS;
  }

  public boolean isFullnameInvalid(Journey journey) {
    return !journey.isApplicantYourself() && StringUtils.isBlank(fullName);
  }
}
