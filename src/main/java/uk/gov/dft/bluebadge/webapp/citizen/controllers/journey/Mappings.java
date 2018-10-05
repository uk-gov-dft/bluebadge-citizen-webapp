package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

@Getter
public class Mappings {
  public static final String URL_ROOT = "/";
  public static final String URL_APPLY_IN_WELSH = "/apply-in-welsh";
  public static final String URL_APPLICANT_TYPE = "/applicant";
  public static final String URL_APPLICANT_NAME = "/name";
  public static final String URL_RECEIVE_BENEFITS = "/benefits";
  public static final String URL_HIGHER_RATE_MOBILITY = "/higher-rate-mobility";
  public static final String URL_ELIGIBLE = "/eligible";
  public static final String URL_MAY_BE_ELIGIBLE = "/may-be-eligible";
  public static final String URL_HEALTH_CONDITIONS = "/health-conditions";
  public static final String URL_DECLARATIONS = "/apply-for-a-blue-badge/declaration";
  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String URL_CHOOSE_YOUR_COUNCIL = "/choose-council";
  public static final String URL_YOUR_ISSUING_AUTHORITY = "/your-issuing-authority";
  public static final String URL_DOB = "/date-of-birth";
  public static final String URL_CONTACT_DETAILS = "/contact-details";
  public static final String URL_GENDER = "/gender";
  public static final String URL_NINO = "/nino";
  public static final String URL_ENTER_ADDRESS = "/enter-address";

  // PIP Journey Routes
  public static final String URL_PIP_PLANNING_JOURNEY = "/planning-and-following";
  public static final String URL_PIP_MOVING_AROUND = "/moving-around";
  public static final String URL_PIP_RECEIVED_DLA = "/dla-in-the-past";

  // AFCS Journey Routes
  public static final String URL_AFCS_COMPENSATION_SCHEME = "/lump-sum";
  public static final String URL_AFCS_DISABILITY = "/permanent-and-substantial-disability";
  public static final String URL_AFCS_MENTAL_DISORDER = "/permanent-mental-disorder";

  // Main reason sub journey
  public static final String URL_MAIN_REASON = "/main-reason";
  public static final String URL_WALKING_DIFFICULTY = "/walking-difficulty";
  public static final String URL_NOT_ELIGIBLE = "/not-eligible";
  public static final String URL_CONTACT_COUNCIL = "/contact-council";

  // Walking difficulties route
  public static final String URL_WHAT_WALKING_DIFFICULT = "/what-makes-walking-difficult";
  public static final String URL_WHERE_CAN_YOU_WALK = "/where-can-you-walk";

  private Mappings() {}

  private static final BiMap<StepDefinition, String> stepToUrlMapping =
      ImmutableBiMap.<StepDefinition, String>builder()
          .put(StepDefinition.HOME, URL_ROOT)
          .put(StepDefinition.APPLICANT_TYPE, URL_APPLICANT_TYPE)
          .put(StepDefinition.DOB, URL_DOB)
          .put(StepDefinition.CONTACT_DETAILS, URL_CONTACT_DETAILS)
          .put(StepDefinition.GENDER, URL_GENDER)
          .put(StepDefinition.NINO, URL_NINO)
          .put(StepDefinition.ADDRESS, URL_ENTER_ADDRESS)
          .put(StepDefinition.NAME, URL_APPLICANT_NAME)
          .put(StepDefinition.RECEIVE_BENEFITS, URL_RECEIVE_BENEFITS)
          .put(StepDefinition.HIGHER_RATE_MOBILITY, URL_HIGHER_RATE_MOBILITY)
          .put(StepDefinition.ELIGIBLE, URL_ELIGIBLE)
          .put(StepDefinition.MAY_BE_ELIGIBLE, URL_MAY_BE_ELIGIBLE)

          // PIP Journey Mappings
          .put(StepDefinition.PIP_MOVING_AROUND, URL_PIP_MOVING_AROUND)
          .put(StepDefinition.PIP_DLA, URL_PIP_RECEIVED_DLA)
          .put(StepDefinition.PIP_PLANNING_JOURNEY, URL_PIP_PLANNING_JOURNEY)

          // AFCS Journey Mappings
          .put(StepDefinition.AFCS_DISABILITY, URL_AFCS_DISABILITY)
          .put(StepDefinition.AFCS_MENTAL_DISORDER, URL_AFCS_MENTAL_DISORDER)
          .put(StepDefinition.AFCS_COMPENSATION_SCHEME, URL_AFCS_COMPENSATION_SCHEME)

          // Main reason mappings
          .put(StepDefinition.MAIN_REASON, URL_MAIN_REASON)
          .put(StepDefinition.CONTACT_COUNCIL, URL_CONTACT_COUNCIL)
          .put(StepDefinition.WALKING_DIFFICULTY, URL_WALKING_DIFFICULTY)
          .put(StepDefinition.WHERE_CAN_YOU_WALK, URL_WHERE_CAN_YOU_WALK)
          .put(StepDefinition.NOT_ELIGIBLE, URL_NOT_ELIGIBLE)
          .put(StepDefinition.HEALTH_CONDITIONS, URL_HEALTH_CONDITIONS)
          .put(StepDefinition.WHAT_WALKING_DIFFICULTIES, URL_WHAT_WALKING_DIFFICULT)
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
