package uk.gov.service.bluebadge.test.acceptance.util;

import java.util.HashMap;
import java.util.Map;

public class CustomState {

  public static final String APPLICANT = "APPLICANT";
  public static final String DOB = "DOB";

  private static ThreadLocal<Map<String, Object>> testState;

  static {
    testState.set(new HashMap<>());
  }

  public static void setState(String key, Object object) {
    testState.get().put(key, object);
  }

  public static Object getState(String key) {
    return testState.get().get(key);
  }
}
