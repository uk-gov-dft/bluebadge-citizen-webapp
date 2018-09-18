package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

@Getter
public class Mappings {
  public static final String URL_ROOT = "/";
  public static final String URL_APPLICANT_TYPE = "/applicant";
  public static final String URL_RECEIVE_BENEFITS = "/benefits";
  public static final String URL_HEALTH_CONDITIONS = "/health-conditions";
  public static final String URL_DECLARATIONS = "/apply-for-a-blue-badge/declaration";
  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";

  private Mappings() {}

  private static final BiMap<StepDefinition, String> stepToUrlMapping =
      ImmutableBiMap.of(
          StepDefinition.HOME,
          URL_ROOT,
          StepDefinition.APPLICANT_TYPE,
          URL_APPLICANT_TYPE,
          StepDefinition.HEALTH_CONDITIONS,
          URL_HEALTH_CONDITIONS,
          StepDefinition.DECLARATIONS,
          URL_DECLARATIONS,
          StepDefinition.SUBMITTED,
          URL_APPLICATION_SUBMITTED);

  public static StepDefinition getStep(String url) {
    return stepToUrlMapping.inverse().get(url);
  }

  public static String getUrl(StepDefinition step) {
    return stepToUrlMapping.get(step);
  }
}
