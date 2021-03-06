package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Benefit {

  private Boolean isIndefinite;
  private LocalDate expiryDate;
}
