package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

public class RedisServiceTest {

  private RedisService service;
  @Mock Jedis mockJedis;

  public RedisServiceTest() {
    MockitoAnnotations.initMocks(this);
    RedisSessionConfig config = new RedisSessionConfig();
    config.setSaveSessionCodeDurationMins(2);
    config.setSaveSessionDurationHours(1);
    config.setSaveSubmitThrottleTimeMins(5);
    config.setSaveSubmitThrottleTries(4);
    service = new RedisService(mockJedis, config);
  }

  @Test
  public void getRedisKeyExpiryInSeconds() {
    assertThat(service.getRedisKeyExpiryInSeconds(JOURNEY)).isEqualTo(3600);
    assertThat(service.getRedisKeyExpiryInSeconds(RedisKeys.CODE)).isEqualTo(120);
    assertThat(service.getRedisKeyExpiryInSeconds(RedisKeys.EMAIL_TRIES)).isEqualTo(300);
    assertThat(service.getRedisKeyExpiryInSeconds(RedisKeys.CODE_TRIES)).isEqualTo(300);
  }

  @Test
  public void setAndExpireIfNew_whenNotNew() {
    String key = JOURNEY.getKey("abc");
    when(mockJedis.exists(key)).thenReturn(true);
    when(mockJedis.ttl(key)).thenReturn(3L);
    service.setAndExpireIfNew(JOURNEY, "abc", "avalue");
    verify(mockJedis, times(1)).set(key, "avalue");
    verify(mockJedis, times(1)).expire(key, 3);
  }

  @Test
  public void setAndExpireIfNew_whenNew() {
    String key = JOURNEY.getKey("newabc");
    when(mockJedis.exists(key)).thenReturn(false);
    service.setAndExpireIfNew(JOURNEY, "newabc", "avalue");
    verify(mockJedis, times(1)).set(key, "avalue");
    verify(mockJedis, times(1)).expire(key, 3600);
  }

  @Test
  public void get() {
    String key = JOURNEY.getKey("get");
    service.get(JOURNEY, "get");
    verify(mockJedis, times(1)).get(key);
  }

  @Test
  public void exists() {
    String key = JOURNEY.getKey("exists");
    service.exists(JOURNEY, "exists");
    verify(mockJedis, times(1)).exists(key);
  }

  @Test
  public void incrementAndSetExpiryIfNew_whenNotNew() {
    String key = JOURNEY.getKey("incr");
    when(mockJedis.incr(key)).thenReturn(2L);
    assertThat(service.incrementAndSetExpiryIfNew(JOURNEY, "incr")).isEqualTo(2L);
    verify(mockJedis, never()).expire(eq(key), anyInt());
    verify(mockJedis, times(1)).incr(key);
  }

  @Test
  public void incrementAndSetExpiryIfNew_whenNew() {
    String key = JOURNEY.getKey("incrNew");
    when(mockJedis.incr(key)).thenReturn(1L);
    assertThat(service.incrementAndSetExpiryIfNew(JOURNEY, "incrNew")).isEqualTo(1L);
    verify(mockJedis, times(1)).expire(key, 3600);
    verify(mockJedis, times(1)).incr(key);
  }

  @Test
  public void throttleExceeded() {
    assertThat(service.throttleExceeded(4L)).isFalse();
    assertThat(service.throttleExceeded(5L)).isTrue();
  }

  @Test
  public void getExpiryTimeFormatted() {
    String key = JOURNEY.getKey("expiryFormatted");
    when(mockJedis.ttl(key)).thenReturn(60L);
    // Should look like; 1 Jan 1970 at 12:30
    String time = service.getExpiryTimeFormatted(JOURNEY, "expiryFormatted");
    Matcher p =
        Pattern.compile("^[0-9]{1,2} [A-Z][a-z]{2} 20[0-9]{2} at [0-9]{1,2}:[0-5][0-9]$")
            .matcher(time);
    assertThat(p.find()).isTrue();
  }

  @Test
  public void getDaysToExpiryRoundedUp() {
    String key = JOURNEY.getKey("days");
    // 1 min
    when(mockJedis.ttl(key)).thenReturn(60L);
    assertThat(service.getDaysToExpiryRoundedUp(JOURNEY, "days")).isEqualTo(0L);

    // Just over half a day
    when(mockJedis.ttl(key)).thenReturn((12L * 60 * 60) + 1);
    assertThat(service.getDaysToExpiryRoundedUp(JOURNEY, "days")).isEqualTo(1L);

    // 4 and a bit days
    when(mockJedis.ttl(key)).thenReturn((96L * 60 * 60) + 1000);
    assertThat(service.getDaysToExpiryRoundedUp(JOURNEY, "days")).isEqualTo(4L);

    // Never
    when(mockJedis.ttl(key)).thenReturn(-1L);
    assertThat(service.getDaysToExpiryRoundedUp(JOURNEY, "days")).isEqualTo(-1L);
  }

  @Test
  public void getHoursToExpiry() {
    String key = JOURNEY.getKey("hours");
    // 1 min
    when(mockJedis.ttl(key)).thenReturn(60L);
    assertThat(service.getHoursToExpiry(JOURNEY, "hours")).isEqualTo(0L);

    // 12 H
    when(mockJedis.ttl(key)).thenReturn(12L * 60 * 60);
    assertThat(service.getHoursToExpiry(JOURNEY, "hours")).isEqualTo(12L);

    // 12 H + a bit
    when(mockJedis.ttl(key)).thenReturn((12L * 60 * 60) + 100);
    assertThat(service.getHoursToExpiry(JOURNEY, "hours")).isEqualTo(12L);

    // Never
    when(mockJedis.ttl(key)).thenReturn(-1L);
    assertThat(service.getHoursToExpiry(JOURNEY, "hours")).isEqualTo(-1L);
  }
}
