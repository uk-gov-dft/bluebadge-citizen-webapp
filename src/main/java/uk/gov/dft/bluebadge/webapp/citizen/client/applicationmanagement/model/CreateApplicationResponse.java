package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.UUID;
import lombok.Data;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Data
public class CreateApplicationResponse extends CommonResponse {
  private UUID data = null;
}
