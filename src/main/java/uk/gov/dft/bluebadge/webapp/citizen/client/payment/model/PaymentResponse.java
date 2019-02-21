package uk.gov.dft.bluebadge.webapp.citizen.client.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse extends CommonResponse implements Serializable {

  @JsonIgnore
  public String getPaymentJourneyUuid() {
    return data.get("paymentJourneyUuid");
  }

  @JsonIgnore
  public String getNextUrl() {
    return data.get("nextUrl");
  }

  @JsonProperty("data")
  Map<String, String> data;

  public Map<String, String> getData() {
    return data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }
}
