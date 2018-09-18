package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChooseYourCouncilForm {

  private String councilShortCode;
}
