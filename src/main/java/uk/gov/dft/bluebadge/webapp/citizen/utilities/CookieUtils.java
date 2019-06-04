package uk.gov.dft.bluebadge.webapp.citizen.utilities;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CookieUtils {

  private HttpServletRequest req;
  public static final String COOKIE_BANNER_KEY = "cookie_banner_seen";

  @Autowired
  public CookieUtils(HttpServletRequest req) {
    this.req = req;
  }

  public Boolean isCookieBannerSet() {
    return WebUtils.getCookie(req, COOKIE_BANNER_KEY) != null;
  }
  public static String extractBuildNumber(String buildVersion) {
    Assert.notNull(buildVersion, "Build version required.");
    Matcher m = Pattern.compile("^v([0-9]{1,}\\.[0-9]{1,}\\.[0-9]{1,}).*").matcher(buildVersion);
    if (m.find() && m.groupCount() == 1) {
      return m.group(1);
    } else {
      return null;
    }
  }
}
