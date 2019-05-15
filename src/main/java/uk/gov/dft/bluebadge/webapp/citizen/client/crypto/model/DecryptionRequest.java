package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecryptionRequest {
  /** Data to decrypt. */
  @JsonProperty @NotEmpty String data;
}
