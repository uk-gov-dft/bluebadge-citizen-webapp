package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;

import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode
public class DecryptionData {
  @NotNull private String decryptResult;
  private Map<String, String> encryptionContext;
}
