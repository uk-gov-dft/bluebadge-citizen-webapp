package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
@Slf4j
public class NotPaidForm implements StepForm, Serializable {

  @NotNull private String retry;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NOT_PAID;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (!"no".equals(retry)) {
      log.warn("NotPaidForm retry value is {}, but must be 'no' to determine next step.", retry);
    }
    return Optional.of(StepDefinition.SUBMITTED);
  }
}
