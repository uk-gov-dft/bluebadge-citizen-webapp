package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Artifact {
  private ArtifactType type;
  private String link;
}
