package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class MobilityAidListForm implements Serializable, StepForm {

  List<MobilityAidAddForm> mobilityAids;

  @NotNull(message = "{NotNull.hasWalkingAid}")
  private Boolean hasWalkingAid;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MOBILITY_AID_LIST;
  }
}
