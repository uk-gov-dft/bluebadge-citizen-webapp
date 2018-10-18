package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WalkingAid {

  private String description;
  private String usage;
  private HowProvidedCodeField howProvidedCode;
}
