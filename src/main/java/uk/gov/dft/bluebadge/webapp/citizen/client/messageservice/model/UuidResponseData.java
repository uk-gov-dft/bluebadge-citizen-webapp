package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class UuidResponseData {
  @JsonProperty("uuid")
  private String uuid;
}
