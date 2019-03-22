package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataDomainEnum.CITIZEN;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceDataResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.SingleReferenceDataResponse;

public class ReferenceDataApiClientTest {
  public static final String TEST_URI = "http://justtesting:8787/test/";

  private static final String BASE_ENDPOINT = TEST_URI + "reference-data";

  private static final String POSTCODE = "AA11AA";

  private ReferenceDataApiClient client;

  private MockRestServiceServer mockServer;

  private ObjectMapper objectMapper = new ObjectMapper();
  private List<ReferenceData> referenceData;

  @Before
  public void setUp() throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    client = new ReferenceDataApiClient(restTemplate);

    Path path =
        Paths.get(getClass().getClassLoader().getResource("mockResponses/refData.json").toURI());
    String json = new String(Files.readAllBytes(path));
    objectMapper.registerModule(new Jdk8Module());
    referenceData = objectMapper.readValue(json, new TypeReference<List<ReferenceData>>() {});
  }

  @Test
  public void retrieveReferenceDataWithADomain_shouldReturnReferenceDataForThatDomain()
      throws Exception {
    ReferenceDataResponse response = new ReferenceDataResponse();
    response.setData(referenceData);
    String responseBody = objectMapper.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "/" + CITIZEN))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

    List<ReferenceData> result = client.retrieveReferenceData(CITIZEN);
    assertThat(result).isEqualTo(referenceData);
  }

  @Test
  @SneakyThrows
  public void retrieveLAByPostcode_shouldReturnLA() {
    ReferenceData la = new ReferenceData();
    la.setShortCode("ABERD");
    la.setDescription("Aberdeenshire");
    SingleReferenceDataResponse response = SingleReferenceDataResponse.builder().data(la).build();
    response.setData(la);
    String responseBody = objectMapper.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "/postcode/" + POSTCODE))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

    assertThat(client.retrieveLAByPostcode(POSTCODE)).isEqualTo(la);
  }

  @Test(expected = IllegalArgumentException.class)
  public void retrieveLAByPostcode_shouldThrowIllegalArgumentException_whenPostcodeIsNull() {
    client.retrieveLAByPostcode(null);
  }
}
