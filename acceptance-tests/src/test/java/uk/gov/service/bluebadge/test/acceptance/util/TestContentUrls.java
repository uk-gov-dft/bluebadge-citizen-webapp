package uk.gov.service.bluebadge.test.acceptance.util;

import java.util.HashMap;
import java.util.Map;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AbstractSitePage;

public class TestContentUrls {

  private final Map<String, String> urlLookup = new HashMap<>();

  public TestContentUrls() {
    setup();
  }

  public String lookupUrl(String pageName) {
    String url = urlLookup.get(pageName.toLowerCase());
    if (url == null) {
      throw new RuntimeException("Unknown pageName: " + pageName);
    }
    return AbstractSitePage.URL + url;
  }

  public static String lookupUrlUnmapped(String pageName) {
    if (pageName.startsWith("/")) {
      return AbstractSitePage.URL + pageName;
    } else {
      return AbstractSitePage.URL + "/" + pageName;
    }
  }

  private void setup() {
    add("applicant", "/applicant");
    add("apply-for-a-badge/declaration", "/apply-for-a-badge/declaration");
  }

  private void add(String pageName, String url) {
    urlLookup.put(pageName.toLowerCase(), url);
  }
}
