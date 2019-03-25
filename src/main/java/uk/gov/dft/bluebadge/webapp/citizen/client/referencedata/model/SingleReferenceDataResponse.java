package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Data
@Builder
public class SingleReferenceDataResponse extends CommonResponse {
  @JsonProperty("data")
  @Valid
  @NonNull
  private ReferenceData data;
}
