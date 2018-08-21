package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Validated
@Data
public class ApplicationResponse extends CommonResponse {
  @JsonProperty("data")
  private Application data = null;

  public ApplicationResponse data(Application data) {
    this.data = data;
    return this;
  }
}
