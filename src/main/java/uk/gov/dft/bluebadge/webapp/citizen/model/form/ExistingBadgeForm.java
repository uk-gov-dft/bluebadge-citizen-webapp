package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ExistingBadgeForm implements StepForm, Serializable {

  @NotNull(message = "{hasExistingBadge.NotNull}")
  private Boolean hasExistingBadge;

  private String badgeNumber;

  public boolean hasBadgeNumber() {
    return StringUtils.isNotBlank(getBadgeNumber());
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.EXISTING_BADGE;
  }
}
