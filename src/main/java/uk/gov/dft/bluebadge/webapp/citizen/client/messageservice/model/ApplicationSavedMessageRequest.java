package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class ApplicationSavedMessageRequest extends GenericMessageRequest {
  public static final String EXPIRY_TIME_KEY = "expiryTime";
  public static final String RETURN_LINK_KEY = "returnLink";
  public static final String TEMPLATE = "APPLICATION_SAVED";

  private ApplicationSavedMessageRequest(
      String template, String emailAddress, Map<String, String> attributes) {
    super(template, emailAddress, attributes);
  }

  public ApplicationSavedMessageRequest(String emailAddress, String expiryTime, String returnLink) {
    this(
        TEMPLATE,
        emailAddress,
        ImmutableMap.of(EXPIRY_TIME_KEY, expiryTime, RETURN_LINK_KEY, returnLink));
  }
}
