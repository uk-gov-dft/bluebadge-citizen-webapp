package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class WhereCanYouWalkForm implements StepForm, Serializable {

  @NotBlank
  @Size(max = 100)
  private String destinationToHome;

  @NotBlank
  @Size(max = 100)
  private String timeToDestination;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WHERE_CAN_YOU_WALK;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("destinationToHome", "timeToDestination");
  }
}
