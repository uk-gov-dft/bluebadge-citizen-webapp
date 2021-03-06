package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.ApplicationSavedMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.messageservice.model.SaveAndReturnCodeMessageRequest;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

public class MessageServiceTest {

  private MessageService service;
  @Mock private MessageApiClient apiClient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    RedisSessionConfig config = new RedisSessionConfig();
    config.setSaveReturnLink("returnLink");
    service = new MessageService(apiClient, config);
  }

  @Test
  public void sendReturnToApplicationCodeEmail() {
    service.sendReturnToApplicationCodeEmail("emailAddress", "1234", "expiry");
    ArgumentCaptor<SaveAndReturnCodeMessageRequest> argumentCaptor =
        ArgumentCaptor.forClass(SaveAndReturnCodeMessageRequest.class);
    verify(apiClient).sendSaveAndReturnCodeMessage(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getEmailAddress()).isEqualTo("emailAddress");

    assertThat(argumentCaptor.getValue().getAttributes())
        .contains(entry(SaveAndReturnCodeMessageRequest.RETURN_CODE_KEY, "1234"));
    assertThat(argumentCaptor.getValue().getAttributes())
        .contains(entry(SaveAndReturnCodeMessageRequest.EXPIRY_TIME_KEY, "expiry"));
  }

  @Test
  public void sendApplicationSavedEmail() {
    service.sendApplicationSavedEmail("emailAddress1", "expiry");
    ArgumentCaptor<ApplicationSavedMessageRequest> argumentCaptor =
        ArgumentCaptor.forClass(ApplicationSavedMessageRequest.class);
    verify(apiClient).sendApplicationSavedEmail(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getEmailAddress()).isEqualTo("emailAddress1");

    assertThat(argumentCaptor.getValue().getAttributes())
        .contains(entry(ApplicationSavedMessageRequest.EXPIRY_TIME_KEY, "expiry"));
    assertThat(argumentCaptor.getValue().getAttributes())
        .contains(entry(ApplicationSavedMessageRequest.RETURN_LINK_KEY, "returnLink"));
  }
}
