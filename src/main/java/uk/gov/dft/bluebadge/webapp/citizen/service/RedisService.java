package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class RedisService {
  private Jedis jedis;
  private RedisSessionConfig redisSessionConfig;

  @Autowired
  public RedisService(Jedis jedis, RedisSessionConfig redisSessionConfig) {
    this.jedis = jedis;
    this.redisSessionConfig = redisSessionConfig;
  }

  int getExpiry(RedisKeys key) {
    switch (key) {
      case CODE:
        return redisSessionConfig.getSaveSessionCodeDurationMins() * 60;
      case JOURNEY:
        return redisSessionConfig.getSaveSessionDurationHours() * 60 * 60;
      case CODE_TRIES:
      case EMAIL_TRIES:
        return redisSessionConfig.getSaveSubmitThrottleTimeMins() * 60;
      default:
        return -1;
    }
  }

  public void setAndExpire(RedisKeys key, String emailAddress, String value) {
    jedis.set(key.getKey(emailAddress), value);
    jedis.expire(key.getKey(emailAddress), getExpiry(key));
  }

  public String get(RedisKeys key, String emailAddress) {
    return jedis.get(key.getKey(emailAddress));
  }

  public boolean exists(RedisKeys key, String emailAddress) {
    return jedis.exists(key.getKey(emailAddress));
  }

  public Long incrementAndSetExpiryIfNew(RedisKeys key, String emailAddress) {
    Long count = jedis.incr(key.getKey(emailAddress));
    if (count.equals(1L)) {
      jedis.expire(key.getKey(emailAddress), getExpiry(key));
    }
    return count;
  }

  public boolean throttleExceeded(Long count) {
    Assert.notNull(count, "Count cant be null.");
    return count.intValue() > redisSessionConfig.getSaveSubmitThrottleTries();
  }

  public String getExpiryTimeFormatted(RedisKeys key, String emailAddress) {
    Long secondsToLive = jedis.ttl(key.getKey(emailAddress));
    if (secondsToLive.equals(-1L)) {
      log.warn("Call to get expiry of redis key when none set, key type: {}", key.name());
      return "Never";
    }
    return OffsetDateTime.now()
        .plusSeconds(secondsToLive)
        .format(DateTimeFormatter.ofPattern("d MMM yyyy 'at' HH:mm"));
  }
}
