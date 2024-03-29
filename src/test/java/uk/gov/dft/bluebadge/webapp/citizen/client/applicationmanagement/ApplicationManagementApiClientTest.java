package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.CommonResponseErrorHandler;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.CreateApplicationResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.BadRequestException;

public class ApplicationManagementApiClientTest {
  private static final String TEST_URI = "http://justtesting:8787/test";
  private static final String APPLICATION_ENDPOINT = "/applications";

  private MockRestServiceServer mockServer;

  private ObjectMapper objectMapper = new ObjectMapper();
  private ApplicationManagementApiClient client;

  @Before
  public void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new CommonResponseErrorHandler(objectMapper));
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    client = new ApplicationManagementApiClient(restTemplate);
  }

  @Test
  public void createApplication() throws Exception {
    UUID createUuid = UUID.randomUUID();
    CreateApplicationResponse applicationResponse = new CreateApplicationResponse();
    applicationResponse.setData(createUuid);
    String response = objectMapper.writeValueAsString(applicationResponse);

    mockServer
        .expect(once(), requestTo(TEST_URI + APPLICATION_ENDPOINT))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

    Application app = Application.builder().applicationId("bob").build();
    CreateApplicationResponse createdApp = client.createApplication(app);
    assertThat(createdApp).isNotNull();
    assertThat(createdApp.getData()).isEqualTo(createUuid);
  }

  @Test
  public void createApplication_badRequest() throws Exception {
    String commonResponseBody = objectMapper.writeValueAsString(new CommonResponse());

    mockServer
        .expect(once(), requestTo(TEST_URI + APPLICATION_ENDPOINT))
        .andExpect(method(HttpMethod.POST))
        .andRespond(
            withBadRequest().body(commonResponseBody).contentType(MediaType.APPLICATION_JSON));

    try {
      client.createApplication(Application.builder().build());
      fail("No exception thrown");
    } catch (BadRequestException e) {
      assertThat(e.getCommonResponse()).isNotNull();
    }
  }
}
