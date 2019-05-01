package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

import javax.validation.Valid;
import java.util.Objects;

/** UuidResponse */
@Validated
@Data
public class UuidResponse extends CommonResponse {
  @JsonProperty("data")
  private UuidResponseData data;
}
