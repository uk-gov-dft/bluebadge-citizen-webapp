package uk.gov.dft.bluebadge.webapp.citizen.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class VersionCookieUtils {

  private RedisSessionConfig redisSessionConfig;

  @Autowired
  public VersionCookieUtils(final RedisSessionConfig redisSessionConfig) {
    this.redisSessionConfig = redisSessionConfig;
  }

  public void removeRedirectCookie(final HttpServletResponse response) {
    response.addCookie(new Cookie(redisSessionConfig.getStoredJourneyVersionCookieName(), ""));
  }
}
