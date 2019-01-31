package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
@JsonTypeInfo(
  defaultImpl = ReferenceData.class,
  use = JsonTypeInfo.Id.NAME,
  property = "groupShortCode",
  visible = true
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = LocalAuthorityRefData.class, name = "LA"),
  @JsonSubTypes.Type(value = LocalCouncilRefData.class, name = "LC")
})
public class ReferenceData implements Serializable {
  private String shortCode;
  private String description;
  private Map<String, Object> metaData;
  private String groupShortCode;
  private String groupDescription;
  private String subgroupShortCode;
  private String subgroupDescription;
  private Integer displayOrder;
}
