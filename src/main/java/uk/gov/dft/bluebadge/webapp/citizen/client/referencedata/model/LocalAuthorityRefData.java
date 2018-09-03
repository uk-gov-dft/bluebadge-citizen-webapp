package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LocalAuthorityRefData extends ReferenceData {
  @JsonProperty("metaData")
  private Optional<LocalAuthorityMetaData> localAuthorityMetaData = Optional.empty();

  @JsonIgnore
  public String getContactUrl() {
    return localAuthorityMetaData.map(LocalAuthorityMetaData::getContactUrl).orElse(null);
  }

  @JsonIgnore
  public String getNation() {
    return localAuthorityMetaData.map(LocalAuthorityMetaData::getNation).orElse(null);
  }

  @Data
  public static class LocalAuthorityMetaData {
    private String issuingAuthorityShortCode;
    private String issuingAuthorityName;
    private String nation;
    private String contactUrl;
  }
}
