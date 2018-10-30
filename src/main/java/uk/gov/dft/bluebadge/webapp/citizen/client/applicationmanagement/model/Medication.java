package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Medication {

  private String name;
  private Boolean isPrescribed;
  private String frequency;
  private String quantity;
}
