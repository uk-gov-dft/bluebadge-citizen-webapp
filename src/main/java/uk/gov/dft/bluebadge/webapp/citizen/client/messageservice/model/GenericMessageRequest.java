package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Map;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class GenericMessageRequest {
  @NonNull private final String template;
  @NonNull private final String emailAddress;
  private final Map<String, ?> attributes;
}
