package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HealthcareProfessional {

  private String name = null;
  private String location = null;
}
