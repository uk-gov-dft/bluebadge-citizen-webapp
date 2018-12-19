package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import java.net.URL;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
public class JourneyArtifact implements Serializable {
  @NonNull private final String fileName;
  @NonNull private final String type;
  @NonNull private final URL url;

  // TODO: Why transient?
  @Setter private transient URL signedUrl;
}
