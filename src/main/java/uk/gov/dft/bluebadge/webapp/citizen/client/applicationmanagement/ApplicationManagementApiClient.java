package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationResponse;

@Slf4j
@Service
public class ApplicationManagementApiClient {

  public static final String CREATE_ENDPOINT = "/applications";
  private final RestTemplate restTemplate;

  @Autowired
  public ApplicationManagementApiClient(
      @Qualifier("applicationManagementServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Application createApplication(Application application) {
    Assert.notNull(application, "createApplication - application must be set");

    HttpEntity<Application> request = new HttpEntity<>(application);
    return Objects.requireNonNull(
            restTemplate.postForObject(CREATE_ENDPOINT, request, ApplicationResponse.class))
        .getData();
  }
}
