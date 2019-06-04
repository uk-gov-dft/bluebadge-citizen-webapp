package uk.gov.dft.bluebadge.webapp.citizen.utilities;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;

@Service
public class RedirectVersionCookieManager {

  private static final int EXPIRY_7_DAYS = 604800;

  private RedisSessionConfig redisSessionConfig;

  @Autowired
  public RedirectVersionCookieManager(final RedisSessionConfig redisSessionConfig) {
    this.redisSessionConfig = redisSessionConfig;
  }

  public void addCookie(final HttpServletResponse response, CryptoVersionException e) {
    response.addCookie(getVersionCookie(e.getEncryptedVersion()));
  }

  public void removeCookie(final HttpServletResponse response) {
    Cookie cookie = getVersionCookie("");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  /**
   * A cookie to set to allow nginx to redirect to correct version of citizen webapp.
   *
   * @param version e.g. 1.0.1
   * @return The cookie.
   */
  private Cookie getVersionCookie(String version) {
    Cookie cookie = new Cookie(redisSessionConfig.getStoredJourneyVersionCookieName(), version);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(EXPIRY_7_DAYS);
    // Default to domain creating cookie (us). i.e. Don't call setDomain
    return cookie;
  }
}
