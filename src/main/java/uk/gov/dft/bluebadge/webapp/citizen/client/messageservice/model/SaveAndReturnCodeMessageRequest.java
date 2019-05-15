package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class SaveAndReturnCodeMessageRequest extends GenericMessageRequest {
  public static final String RETURN_CODE_KEY = "returnCode";
  public static final String EXPIRY_TIME_KEY = "expiryTime";
  public static final String TEMPLATE = "SAVE_AND_RETURN";

  private SaveAndReturnCodeMessageRequest(
      String template, String emailAddress, Map<String, String> attributes) {
    super(template, emailAddress, attributes);
  }

  public SaveAndReturnCodeMessageRequest(String emailAddress, String code, String expiryTime) {
    super(
        TEMPLATE,
        emailAddress,
        ImmutableMap.of(RETURN_CODE_KEY, code, EXPIRY_TIME_KEY, expiryTime));
  }
}
