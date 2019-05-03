package uk.gov.dft.bluebadge.webapp.citizen.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

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

  /**
   * Get expiry in seconds based upon key type.
   *
   * @param key Redis key type.
   * @return The lifetime of the key.
   */
  int getRedisKeyExpiryInSeconds(RedisKeys key) {
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

  /**
   * Set a value and also set the expiry (as from application config).
   *
   * @param key Redis key type
   * @param emailAddress Email address used to generate redis key
   * @param value Value to persist
   */
  public void setAndExpire(RedisKeys key, String emailAddress, String value) {
    jedis.set(key.getKey(emailAddress), value);
    jedis.expire(key.getKey(emailAddress), getRedisKeyExpiryInSeconds(key));
  }

  /**
   * Create ans set expiry, or update and leave expiry of original object.
   *
   * @param key Redis key type
   * @param emailAddress Email address.
   * @param value Value to persist.
   */
  public void setAndExpireIfNew(RedisKeys key, String emailAddress, String value) {
    if (exists(key, emailAddress)) {
      Long ttl = jedis.ttl(key.getKey(emailAddress));
      jedis.set(key.getKey(emailAddress), value);
      jedis.expire(key.getKey(emailAddress), ttl.intValue());
    } else {
      setAndExpire(key, emailAddress, value);
    }
  }

  /**
   * Retrieve value from redis.
   *
   * @param key Redis key type
   * @param emailAddress Email address used to create key
   * @return The value from Redis
   */
  public String get(RedisKeys key, String emailAddress) {
    return jedis.get(key.getKey(emailAddress));
  }

  /**
   * Key exists in redis.
   *
   * @param key Redis key type
   * @param emailAddress Email address used to generate key
   * @return True if exists
   */
  public boolean exists(RedisKeys key, String emailAddress) {
    return jedis.exists(key.getKey(emailAddress));
  }

  /**
   * Increment value. If doesn't exist, sets to 1.
   *
   * @param key Redis key type.
   * @param emailAddress Email address used to generate key.
   * @return New, incremented value.
   */
  public Long incrementAndSetExpiryIfNew(RedisKeys key, String emailAddress) {
    Long count = jedis.incr(key.getKey(emailAddress));
    if (count.equals(1L)) {
      jedis.expire(key.getKey(emailAddress), getRedisKeyExpiryInSeconds(key));
    }
    return count;
  }

  /**
   * Number of attempts exceeded. Based upon application config. Shared count value in config at the
   * mo
   *
   * @param count Number of tries to compare against
   * @return True if throttle exceeded
   */
  public boolean throttleExceeded(Long count) {
    Assert.notNull(count, "Count cant be null.");
    return count.intValue() > redisSessionConfig.getSaveSubmitThrottleTries();
  }

  /**
   * Get expiry time of Redis key in format for email/display to user.
   *
   * @param key Redis key type
   * @param emailAddress Email address used to generate key
   * @return Friendly formatted time.
   */
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
