package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.ApplicationManagementApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.CreateApplicationResponse;

public class ApplicationManagementServiceTest {

  @Mock private ApplicationManagementApiClient clientMock;

  private ApplicationManagementService appService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    appService = new ApplicationManagementService(clientMock);
  }

  @Test
  public void createApplication_WithCorrectApplicationDetails() {
    CreateApplicationResponse response = new CreateApplicationResponse();
    UUID createUuid = UUID.fromString("36b93430-11e4-4694-b67c-b15f6fd2ea2e");
    response.setData(createUuid);

    when(clientMock.createApplication(ApplicationTestData.APPLICATION)).thenReturn(response);

    UUID uuid = appService.create(ApplicationTestData.APPLICATION);
    assertThat(uuid).isEqualTo(createUuid);
  }
}
