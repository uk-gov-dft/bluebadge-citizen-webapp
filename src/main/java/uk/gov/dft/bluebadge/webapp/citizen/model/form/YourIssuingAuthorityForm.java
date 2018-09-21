package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YourIssuingAuthorityForm implements Serializable {
  String localAuthorityDescription;
  String localAuthorityShortCode;
}
