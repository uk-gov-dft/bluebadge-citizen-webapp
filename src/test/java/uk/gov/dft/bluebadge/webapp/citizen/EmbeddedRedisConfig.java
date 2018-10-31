package uk.gov.dft.bluebadge.webapp.citizen;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import redis.embedded.RedisServer;

//@Component
@Slf4j
public class EmbeddedRedisConfig {
  @Value("${spring.redis.port}")
  private int redisPort;

  private RedisServer redisServer;

  @PostConstruct
  public void startRedis() throws IOException {
    log.info("Starting embedded redis on port:{}", redisPort);
    redisServer = new RedisServer(redisPort);
    try {
      redisServer.start();
    } catch (Exception e) {
      log.error("Failed to start embedded redis.", e);
      throw e;
    }
  }

  @PreDestroy
  public void stopRedis() {
    redisServer.stop();
  }
}
