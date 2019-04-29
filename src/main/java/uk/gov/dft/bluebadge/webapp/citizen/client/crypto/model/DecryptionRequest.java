package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@Builder
public class DecryptionRequest {
  /** Data to decrypt. */
  @JsonProperty @NotEmpty String data;
}
