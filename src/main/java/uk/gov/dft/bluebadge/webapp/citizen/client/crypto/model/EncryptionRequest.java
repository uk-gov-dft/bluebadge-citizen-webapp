package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptionRequest {
  /** Data to encrypt. */
  @JsonProperty @NotEmpty String data;
  /**
   * context strings to use to encrypt. Note: The same set of strings required to decrypt.
   *
   * <p>From AWS docs; When an encryption context is provided in an encryption request, it is
   * cryptographically bound to the ciphertext such that the same encryption context is required to
   * decrypt (or decrypt and re-encrypt) the data. If the encryption context provided in the
   * decryption request is not an exact, case-sensitive match, the decrypt request fails. Only the
   * order of the encryption context pairs can vary.
   */
  @JsonProperty Map<String, String> context;
}
