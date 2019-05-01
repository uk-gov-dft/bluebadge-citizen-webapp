package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class SaveAndReturnCodeMessageRequest extends GenericMessageRequest {
  private SaveAndReturnCodeMessageRequest(
      String template, String emailAddress, Map<String, ?> attributes) {
    super(template, emailAddress, attributes);
  }

  public SaveAndReturnCodeMessageRequest(String emailAddress, String code, String expiryTime) {
    super(
        "SAVE_AND_RETURN",
        emailAddress,
        ImmutableMap.of("returnCode", code, "expiryTime", expiryTime));
  }
}
