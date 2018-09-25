package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

@Getter
public class Mappings {
  public static final String URL_ROOT = "/";
  public static final String URL_APPLICANT_TYPE = "/applicant";
  public static final String URL_APPLICANT_NAME = "/name";
  public static final String URL_RECEIVE_BENEFITS = "/benefits";
  public static final String URL_ELIGIBLE = "/eligible";
  public static final String URL_MAY_BE_ELIGIBLE = "/may-be-eligible";
  public static final String URL_HEALTH_CONDITIONS = "/health-conditions";
  public static final String URL_DECLARATIONS = "/apply-for-a-blue-badge/declaration";
  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String URL_CHOOSE_YOUR_COUNCIL = "/choose-council";
  public static final String URL_YOUR_ISSUING_AUTHORITY = "/your-issuing-authority";

  // AFCS Journey Routes
  public static final String URL_AFCS_COMPENSATION_SCHEME = "/lump-sum";
  public static final String URL_AFCS_DISABILITY = "/permanent-and-substantial-disability";
  public static final String URL_AFCS_MENTAL_DISORDER = "/permanent-mental-disorder";

  private Mappings() {}

  private static final BiMap<StepDefinition, String> stepToUrlMapping =
      ImmutableBiMap.<StepDefinition, String>builder()
          .put(StepDefinition.HOME, URL_ROOT)
          .put(StepDefinition.APPLICANT_TYPE, URL_APPLICANT_TYPE)
          .put(StepDefinition.RECEIVE_BENEFITS, URL_RECEIVE_BENEFITS)
          .put(StepDefinition.AFCS_COMPENSATION_SCHEME, URL_AFCS_COMPENSATION_SCHEME)
          .put(StepDefinition.ELIGIBLE, URL_ELIGIBLE)

          // AFCS Journey Mappings
          .put(StepDefinition.MAY_BE_ELIGIBLE, URL_MAY_BE_ELIGIBLE)
          .put(StepDefinition.AFCS_DISABILITY, URL_AFCS_DISABILITY)
          .put(StepDefinition.AFCS_MENTAL_DISORDER, URL_AFCS_MENTAL_DISORDER)
          .put(StepDefinition.NAME, URL_APPLICANT_NAME)
          .put(StepDefinition.HEALTH_CONDITIONS, URL_HEALTH_CONDITIONS)
          .put(StepDefinition.CHOOSE_COUNCIL, URL_CHOOSE_YOUR_COUNCIL)
          .put(StepDefinition.YOUR_ISSUING_AUTHORITY, URL_YOUR_ISSUING_AUTHORITY)
          .put(StepDefinition.DECLARATIONS, URL_DECLARATIONS)
          .put(StepDefinition.SUBMITTED, URL_APPLICATION_SUBMITTED)
          .build();

  public static StepDefinition getStep(String url) {
    return stepToUrlMapping.inverse().get(url);
  }

  public static String getUrl(StepDefinition step) {
    if (!stepToUrlMapping.containsKey(step)) {
      throw new IllegalArgumentException("No URL mapping found for step:" + step);
    }
    return stepToUrlMapping.get(step);
  }
}
