package uk.gov.service.bluebadge.test.acceptance.util;

import java.util.HashMap;
import java.util.Map;

public class TestContentUrls {

  public static final String URL = System.getProperty("baseUrl");
  private final Map<String, String> urlLookup = new HashMap<>();

  public TestContentUrls() {
    setup();
  }

  public String lookupUrl(String pageName) {
    String url = urlLookup.get(pageName.toLowerCase());
    if (url == null) {
      throw new RuntimeException("Unknown pageName: " + pageName);
    }
    return URL + url;
  }

  public static String lookupUrlUnmapped(String pageName) {
    if (pageName.startsWith("/")) {
      return URL + pageName;
    } else {
      return URL + "/" + pageName;
    }
  }

  private void setup() {
    add("applicant", "/");
    add("describe health conditions", "/health-conditions");
    add("declaration", "/apply-for-a-blue-badge/declaration");
  }

  private void add(String pageName, String url) {
    urlLookup.put(pageName.toLowerCase(), url);
  }
}
