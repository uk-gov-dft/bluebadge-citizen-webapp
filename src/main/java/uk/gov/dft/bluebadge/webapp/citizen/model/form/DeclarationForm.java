package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeclarationForm {

  @AssertTrue(message = "{declarationPage.validation.declaration}")
  private Boolean agreed;
}
