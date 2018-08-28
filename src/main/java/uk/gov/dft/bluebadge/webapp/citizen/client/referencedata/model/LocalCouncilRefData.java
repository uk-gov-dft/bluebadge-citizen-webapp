package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocalCouncilRefData extends ReferenceData {
  @JsonProperty("metaData")
  private Optional<LocalCouncilMetaData> localCouncilMetaData;

  @JsonIgnore
  public String getIssuingAuthorityShortCode() {
    return localCouncilMetaData
        .map(LocalCouncilMetaData::getIssuingAuthorityShortCode)
        .orElse(null);
  }

  @Data
  public static class LocalCouncilMetaData {
    private String issuingAuthorityShortCode;
  }
}
