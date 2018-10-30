package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class MobilityAidListForm implements Serializable, StepForm {

  List<MobilityAidAddForm> mobilityAids;

  @NotNull(message = "{NotNull.hasWalkingAid}")
  private String hasWalkingAid;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MOBILITY_AID_LIST;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
