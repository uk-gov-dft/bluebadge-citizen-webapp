package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

@Getter
public class Mappings {
  public static final String URL_ROOT = "/";
  public static final String URL_APPLICANT_TYPE = "/applicant";
  public static final String URL_APPLICANT_NAME = "/name";
  public static final String URL_HEALTH_CONDITIONS = "/health-conditions";
  public static final String URL_DECLARATIONS = "/apply-for-a-blue-badge/declaration";
  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String URL_CHOOSE_YOUR_COUNCIL = "/choose-council";
  public static final String URL_YOUR_ISSUING_AUTHORITY = "/your-issuing-authority";

  private Mappings() {}

  private static final BiMap<StepDefinition, String> stepToUrlMapping =
      new ImmutableBiMap.Builder<StepDefinition, String>()
          .put(StepDefinition.HOME, URL_ROOT)
          .put(StepDefinition.APPLICANT_TYPE, URL_APPLICANT_TYPE)
          .put(StepDefinition.HEALTH_CONDITIONS, URL_HEALTH_CONDITIONS)
          .put(StepDefinition.CHOOSE_COUNCIL, URL_CHOOSE_YOUR_COUNCIL)
          .put(StepDefinition.YOUR_ISSUING_AUTHORITY, URL_YOUR_ISSUING_AUTHORITY)
          .put(StepDefinition.NAME, URL_APPLICANT_NAME)
          .put(StepDefinition.DECLARATIONS, URL_DECLARATIONS)
          .put(StepDefinition.SUBMITTED, URL_APPLICATION_SUBMITTED)
          .build();

  public static StepDefinition getStep(String url) {
    return stepToUrlMapping.inverse().get(url);
  }

  public static String getUrl(StepDefinition step) {
    return stepToUrlMapping.get(step);
  }
}
