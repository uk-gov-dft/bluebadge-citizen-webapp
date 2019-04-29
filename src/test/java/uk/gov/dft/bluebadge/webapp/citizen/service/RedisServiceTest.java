package uk.gov.dft.bluebadge.webapp.citizen.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisServiceTest {

  RedisService service;
  @Mock
  Jedis mockJedis;

  public RedisServiceTest() {
    MockitoAnnotations.initMocks(this);
    service = new RedisService(mockJedis, new RedisSessionConfig());
  }

  @Test
  public void getJourneySaveForReturnKey() {
    String key = service.getJourneySaveForReturnKey("bob@email.com");
    assertThat(key).isEqualTo("bob@email.com:citizen-application:save-and-return-journey");
  }

  @Test
  public void get4DigitCode() {
    int code = service.get4DigitCode("A relatively short string");
    assertThat(code).isEqualTo(4112);
  }
}