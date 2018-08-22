package uk.gov.dft.bluebadge.webapp.citizen.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.BadRequestException;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.NotFoundException;

public class CommonResponseErrorHandlerTest {
  private CommonResponseErrorHandler errorHandler;
  private RestTemplate restTemplate;
  private MockRestServiceServer mockServer;
  private ObjectMapper objectMapper;

  @Before
  public void setup(){
    objectMapper = new ObjectMapper();
    errorHandler = new CommonResponseErrorHandler(objectMapper);
    restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(errorHandler);
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
  }

  @Test
  @SneakyThrows
  public void badRequestsHandled(){
    String commonResponseBody = objectMapper.writeValueAsString(new CommonResponse());
    mockServer
        .expect(once(), requestTo("/bob"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withBadRequest().body(commonResponseBody).contentType(MediaType.APPLICATION_JSON));

    try {
      restTemplate.getForObject("/bob", String.class);
      fail("no exception thrown");
    } catch (BadRequestException e) {
      assertThat(e.getCommonResponse()).isNotNull();
    }
  }

  @Test
  @SneakyThrows
  public void notFoundHandled(){
    String commonResponseBody = objectMapper.writeValueAsString(new CommonResponse());
    mockServer
        .expect(once(), requestTo("/bob"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.NOT_FOUND).body(commonResponseBody).contentType(MediaType.APPLICATION_JSON));

    try {
      restTemplate.getForObject("/bob", String.class);
      fail("no exception thrown");
    } catch (NotFoundException e) {
      assertThat(e.getCommonResponse()).isNotNull();
    }
  }

  @Test
  @SneakyThrows
  public void internalServerErrorsHandledByDefaultHandler(){
    mockServer
        .expect(once(), requestTo("/bob"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

    try {
      restTemplate.getForObject("/bob", String.class);
      fail("no exception thrown");
    } catch (HttpServerErrorException e) {
      // pass
    }
  }
}