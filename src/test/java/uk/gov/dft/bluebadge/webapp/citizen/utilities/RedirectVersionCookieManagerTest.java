package uk.gov.dft.bluebadge.webapp.citizen.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.info.BuildProperties;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;

public class RedirectVersionCookieManagerTest {
  private static final int EXPIRY_7_DAYS = 604800;
  private static final String COOKIE_NAME = "myStoredJourneyVersionCookieName";
  private static final String ENCRYPTED_VERSION = "10";

  @Mock private RedisSessionConfig mockRedisSessionConfig;
  @Mock private HttpServletResponse mockResponse;
  @Mock private CryptoVersionException mockException;
  @Mock private BuildProperties mockBuildProperties;

  private RedirectVersionCookieManager cookieManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    cookieManager = new RedirectVersionCookieManager(mockRedisSessionConfig, mockBuildProperties);
    when(mockRedisSessionConfig.getStoredJourneyVersionCookieName()).thenReturn(COOKIE_NAME);
    when(mockException.getEncryptedVersion()).thenReturn(ENCRYPTED_VERSION);
  }

  @Test
  public void addCookie_shouldWork() {
    cookieManager.addCookie(mockResponse, mockException);
    Cookie expectedCookie = new Cookie(COOKIE_NAME, ENCRYPTED_VERSION);
    expectedCookie.setMaxAge(EXPIRY_7_DAYS);
    expectedCookie.setHttpOnly(true);
    expectedCookie.setSecure(true);
    ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);
    verify(mockResponse).addCookie(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getName()).isEqualTo(COOKIE_NAME);
    assertThat(argumentCaptor.getValue().getValue()).isEqualTo(ENCRYPTED_VERSION);
    assertThat(argumentCaptor.getValue().getMaxAge()).isEqualTo(EXPIRY_7_DAYS);
    assertThat(argumentCaptor.getValue().getSecure()).isEqualTo(true);
    assertThat(argumentCaptor.getValue().isHttpOnly()).isEqualTo(true);
  }

  @Test
  public void removeCookie_shouldWork() {
    cookieManager.removeCookie(mockResponse);
    Cookie expectedCookie = new Cookie(COOKIE_NAME, "");
    ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);
    verify(mockResponse).addCookie(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getName()).isEqualTo(COOKIE_NAME);
    assertThat(argumentCaptor.getValue().getValue()).isEqualTo("");
  }
}
