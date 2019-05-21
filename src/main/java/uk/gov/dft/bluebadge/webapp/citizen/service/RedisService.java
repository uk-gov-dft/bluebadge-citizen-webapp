package uk.gov.dft.bluebadge.webapp.citizen.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

@Slf4j
@Service
public class RedisService {
  private StringRedisTemplate redisTemplate;
  private RedisSessionConfig redisSessionConfig;

  @Autowired
  public RedisService(StringRedisTemplate redisTemplate, RedisSessionConfig redisSessionConfig) {
    this.redisTemplate = redisTemplate;
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
    redisTemplate
        .opsForValue()
        .set(key.getKey(emailAddress), value, getRedisKeyExpiryInSeconds(key), TimeUnit.SECONDS);
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
      Long ttl = redisTemplate.getExpire(key.getKey(emailAddress));
      redisTemplate.opsForValue().set(key.getKey(emailAddress), value, ttl, TimeUnit.SECONDS);
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
    return redisTemplate.opsForValue().get(key.getKey(emailAddress));
  }

  /**
   * Key exists in redis.
   *
   * @param key Redis key type
   * @param emailAddress Email address used to generate key
   * @return True if exists
   */
  public boolean exists(RedisKeys key, String emailAddress) {
    return redisTemplate.hasKey(key.getKey(emailAddress));
  }

  /**
   * Increment value. If doesn't exist, sets to 1.
   *
   * @param key Redis key type.
   * @param emailAddress Email address used to generate key.
   * @return New, incremented value.
   */
  public Long incrementAndSetExpiryIfNew(RedisKeys key, String emailAddress) {
    if (exists(key, emailAddress)) {
      return redisTemplate.opsForValue().increment(key.getKey(emailAddress));
    } else {
      redisTemplate
          .opsForValue()
          .set(key.getKey(emailAddress), "1", getRedisKeyExpiryInSeconds(key), TimeUnit.SECONDS);
      return 1L;
    }
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

    Long secondsToLive = redisTemplate.getExpire(key.getKey(emailAddress));
    if (null == secondsToLive || secondsToLive.equals(-1L)) {
      log.warn("Call to get expiry of redis key when none set, key type: {}", key.name());
      return "";
    }
    return ZonedDateTime.now(ZoneId.of("Europe/London"))
        .plusSeconds(secondsToLive)
        .format(DateTimeFormatter.ofPattern("d MMM yyyy 'at' HH:mm"));
  }

  public long getDaysToExpiryRoundedUp(RedisKeys key, String emailAddress) {
    Long secondsToLive = redisTemplate.getExpire(key.getKey(emailAddress));
    if (null == secondsToLive || secondsToLive < 0) {
      log.warn("Call to getDaysToExpiryRoundedUp when no expiry");
      return -1;
    }
    long halfDays = secondsToLive / (12 * 60 * 60);

    if (halfDays % 2 == 1) {
      return (halfDays / 2) + 1;
    } else {
      return halfDays / 2;
    }
  }

  public long getHoursToExpiry(RedisKeys key, String emailAddress) {
    Long secondsToLive = redisTemplate.getExpire(key.getKey(emailAddress));
    if (null == secondsToLive || secondsToLive < 0) {
      log.warn("Call to getHoursToExpiry when no expiry");
      return -1;
    }
    return secondsToLive / (60 * 60);
  }
}
