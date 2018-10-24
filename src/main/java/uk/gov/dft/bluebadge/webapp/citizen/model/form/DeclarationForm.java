package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Builder
@Data
@EqualsAndHashCode
public class DeclarationForm implements StepForm, Serializable {

  @AssertTrue(message = "{declarationPage.validation.declaration}")
  private Boolean agreed;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.DECLARATIONS;
  }
}
