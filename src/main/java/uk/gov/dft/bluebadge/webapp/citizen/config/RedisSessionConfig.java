package uk.gov.dft.bluebadge.webapp.citizen.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import redis.clients.jedis.Jedis;

@Configuration
@Data
public class RedisSessionConfig {

  @Value("${spring.redis.host:#{null}}")
  private String host;

  @Value("${spring.redis.port:#{null}}")
  private Integer port;

  @Value("${session.save-expires-after-hours:168}")
  private int saveSessionDurationHours;

  @Value("${session.save-code-expires-after-mins:30}")
  private int saveSessionCodeDurationMins;

  @Value("${session.save-submit-throttle-time-mins:5}")
  private int saveSubmitThrottleTimeMins;

  @Value("${session.save-submit-throttle-tries:3}")
  private int saveSubmitThrottleTries;

  @Bean
  public static ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
  }

  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    cookieSerializer.setSameSite(null);
    return cookieSerializer;
  }

  @Bean
  public Jedis jedis() {
    if (null == host && null == port) {
      return new Jedis();
    }
    if (null == port) {
      return new Jedis(host);
    }
    return new Jedis(host, port);
  }
}
