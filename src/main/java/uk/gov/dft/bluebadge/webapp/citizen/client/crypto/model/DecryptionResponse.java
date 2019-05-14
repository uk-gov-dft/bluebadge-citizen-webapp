package uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Builder
@Data
@EqualsAndHashCode
public class DecryptionResponse extends CommonResponse {
  @NonNull private DecryptionData data;
}
