package uk.gov.dft.bluebadge.webapp.citizen.client.messageservice;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.ApplicationSavedMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.GenericMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.SaveAndReturnCodeMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.UuidResponse;

@Slf4j
@Service
public class MessageApiClient {
  private static final String SEND_MESSAGE_URL = "/messages";

  private final RestTemplate restTemplate;

  @Autowired
  public MessageApiClient(@Qualifier("messageServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private UuidResponse sendMessage(GenericMessageRequest messageRequest) {
    return restTemplate.postForObject(SEND_MESSAGE_URL, messageRequest, UuidResponse.class);
  }

  public UUID sendSaveAndReturnCodeMessage(SaveAndReturnCodeMessageRequest codeRequest) {
    log.debug("Calling message service to request email save and return code.");
    Assert.notNull(codeRequest, "must be set");
    UuidResponse response = sendMessage(codeRequest);
    return UUID.fromString(response.getData().getUuid());
  }

  public UUID sendApplicationSavedEmail(ApplicationSavedMessageRequest request) {
    log.debug("Calling message service to confirm application saved.");
    Assert.notNull(request, "must be set");
    UuidResponse response = sendMessage(request);
    return UUID.fromString(response.getData().getUuid());
  }
}
