package uk.gov.dft.bluebadge.webapp.citizen.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

public class RedisServiceTest {

  RedisService service;
  @Mock Jedis mockJedis;

  public RedisServiceTest() {
    MockitoAnnotations.initMocks(this);
    service = new RedisService(mockJedis, new RedisSessionConfig());
  }
}
