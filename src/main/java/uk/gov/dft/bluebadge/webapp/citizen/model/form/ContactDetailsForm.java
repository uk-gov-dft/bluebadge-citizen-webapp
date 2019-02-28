package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.EMPTY_OR_PHONE_NUMBER;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class ContactDetailsForm implements StepForm, Serializable {

  @Size(max = 100)
  private String fullName;

  @Size(max = 20)
  @NotBlank
  @Pattern(regexp = EMPTY_OR_PHONE_NUMBER, message = "{Invalid.primaryPhoneNumber}")
  private String primaryPhoneNumber;

  @Size(max = 20)
  @Pattern(regexp = EMPTY_OR_PHONE_NUMBER, message = "{Invalid.secondaryPhoneNumber}")
  private String secondaryPhoneNumber;

  @Size(max = 100, message = "Size.emailAddress")
  private String emailAddress;

  private Boolean ignoreEmailAddress;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CONTACT_DETAILS;
  }

  public boolean isFullnameInvalid(Journey journey) {
    return !journey.isApplicantYourself() && StringUtils.isBlank(fullName);
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    EligibilityCodeField benefitType = journey.getEligibilityCode();
    switch (benefitType) {
      case DLA:
      case PIP:
        return Optional.of(StepDefinition.PROVE_BENEFIT);
      case WPMS:
        return Optional.of(StepDefinition.PROVE_IDENTITY);
      case AFRFCS:
        return Optional.of(StepDefinition.PROVE_IDENTITY);
      case BLIND:
        return Optional.of(StepDefinition.REGISTERED);
      default:
        return Optional.of(StepDefinition.HEALTH_CONDITIONS);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of(
        "fullName", "emailAddress", "primaryPhoneNumber", "secondaryPhoneNumber");
  }
}
