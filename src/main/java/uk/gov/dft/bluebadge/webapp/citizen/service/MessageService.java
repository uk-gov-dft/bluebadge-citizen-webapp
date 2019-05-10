package uk.gov.dft.bluebadge.webapp.citizen.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.ApplicationSavedMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.SaveAndReturnCodeMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

@Slf4j
@Service
public class MessageService {

  private MessageApiClient api;
  private RedisSessionConfig redisSessionConfig;

  @Autowired
  MessageService(MessageApiClient api, RedisSessionConfig redisSessionConfig) {
    this.api = api;
    this.redisSessionConfig = redisSessionConfig;
  }

  public void sendReturnToApplicationCodeEmail(
      String emailAddress, String code, String expiryTime) {
    Assert.notNull(emailAddress, "Email address required for send email.");
    Assert.notNull(code, "Code required for send email.");
    Assert.notNull(expiryTime, "Expiry time required for send message.");
    log.info("Sending save and return code via email.");
    UUID result =
        api.sendSaveAndReturnCodeMessage(
            new SaveAndReturnCodeMessageRequest(emailAddress, code, expiryTime));
    log.info("Message service result {}", result);
  }

  public void sendApplicationSavedEmail(String emailAddress, String expiryTime) {
    Assert.notNull(emailAddress, "Email address required for send email.");
    Assert.notNull(expiryTime, "Expiry time required for send message.");
    log.info("Sending save and return code via email.");
    UUID result =
        api.sendApplicationSavedEmail(
            new ApplicationSavedMessageRequest(
                emailAddress, expiryTime, redisSessionConfig.getSaveReturnLink()));
    log.info("Message service result {}", result);
  }
}
