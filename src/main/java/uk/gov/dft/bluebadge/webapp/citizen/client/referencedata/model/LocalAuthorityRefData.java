package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LocalAuthorityRefData extends ReferenceData implements WelshSupport {

  @JsonProperty("metaData")
  private LocalAuthorityMetaData localAuthorityMetaData = null;

  @JsonIgnore
  public String getContactUrl() {
    return getLocalAuthorityMetaData().map(LocalAuthorityMetaData::getContactUrl).orElse(null);
  }

  @JsonIgnore
  public Nation getNation() {
    return getLocalAuthorityMetaData().map(LocalAuthorityMetaData::getNation).orElse(null);
  }

  public Optional<LocalAuthorityMetaData> getLocalAuthorityMetaData() {
    return Optional.ofNullable(localAuthorityMetaData);
  }

  @Override
  public String getDescriptionWelsh() {
    if(getLocalAuthorityMetaData().isPresent()){
      return getLocalAuthorityMetaData().get().welshDescription;
    }
    return null;
  }

  @Data
  public static class LocalAuthorityMetaData implements Serializable {
    private String issuingAuthorityShortCode;
    private String issuingAuthorityName;
    private Nation nation;
    private String contactUrl;
    private String differentServiceSignpostUrl;
    private String welshDescription;
  }
}
