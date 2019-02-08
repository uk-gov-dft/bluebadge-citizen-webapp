package uk.gov.dft.bluebadge.webapp.citizen.client.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

public class PaymentStatusResponse extends CommonResponse implements Serializable {

  @JsonIgnore
  public String getPaymentJourneyUuid() {
    return (String) data.get("paymentJourneyUuid");
  }

  @JsonIgnore
  public String getStatus() {
    return (String) data.get("status");
  }

  @JsonIgnore
  public String getReference() {
    return (String) data.get("reference");
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
