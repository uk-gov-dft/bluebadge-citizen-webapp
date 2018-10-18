package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Benefit {

  private Boolean isIndefinite;
  private LocalDate expiryDate;
}
