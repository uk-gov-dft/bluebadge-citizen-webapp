package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChooseYourCouncilForm {

  @NotBlank(message = "{councilShortCode.NotBank}")
  private String councilShortCode;
}
