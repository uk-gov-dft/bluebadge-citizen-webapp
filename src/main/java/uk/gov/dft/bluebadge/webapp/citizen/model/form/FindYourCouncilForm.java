package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.POSTCODE_CASE_INSENSITIVE;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class FindYourCouncilForm implements StepForm, Serializable {

  @Pattern(regexp = POSTCODE_CASE_INSENSITIVE)
  private String postcode;

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (StringUtils.isBlank(postcode)) {
      return Optional.of(StepDefinition.CHOOSE_COUNCIL);
    } else {
      if (journey.isLocalAuthorityActive()) {
        return Optional.of(StepDefinition.YOUR_ISSUING_AUTHORITY);
      }
      return Optional.of(StepDefinition.DIFFERENT_SERVICE_SIGNPOST);
    }
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.FIND_COUNCIL;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
