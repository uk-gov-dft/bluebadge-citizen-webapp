package uk.gov.dft.bluebadge.webapp.citizen.client.payment.model;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
  @NotBlank private String laShortCode;
  @NotBlank private String returnUrl;
  @NotBlank private String paymentMessage;
  private String language;
}
