package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.net.URL;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Document {
  @NonNull private final String fileName;
  @NonNull private final String type;
  @NonNull private final URL url;
  @NonNull private final URL signedUrl;
}
