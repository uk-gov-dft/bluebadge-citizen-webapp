package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class GenericMessageRequest {
  @NonNull private final String template;
  @NonNull private final String emailAddress;
  private final Map<String, String> attributes;
}
