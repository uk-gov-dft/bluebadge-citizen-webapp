package uk.gov.dft.bluebadge.webapp.citizen.client.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponse extends CommonResponse implements Serializable {

  @JsonIgnore
  public String getPaymentJourneyUuid() {
    return data.get("paymentJourneyUuid");
  }

  @JsonIgnore
  public String getStatus() {
    return data.get("status");
  }

  @JsonIgnore
  public String getReference() {
    return data.get("reference");
  }

  @JsonProperty("data")
  Map<String, String> data;

  void setData(Map<String, String> data) {
    this.data = data;
  }

  Map<String, String> getData() {
    return data;
  }
}
