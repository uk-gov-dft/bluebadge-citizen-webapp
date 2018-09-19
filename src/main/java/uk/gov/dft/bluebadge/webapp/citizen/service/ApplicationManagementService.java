package uk.gov.dft.bluebadge.webapp.citizen.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.ApplicationManagementApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;

@Service
public class ApplicationManagementService {

  private ApplicationManagementApiClient client;

  public ApplicationManagementService(ApplicationManagementApiClient applicationClient) {
    client = applicationClient;
  }

  public UUID create(Application app) {
    return client.createApplication(app).getData();
  }
}
