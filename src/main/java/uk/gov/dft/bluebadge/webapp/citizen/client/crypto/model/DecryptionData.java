package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Builder
@Data
@EqualsAndHashCode
public class DecryptionData {
  @NotNull
  private String decryptResult;
  private Map<String, String> encryptionContext;
}
