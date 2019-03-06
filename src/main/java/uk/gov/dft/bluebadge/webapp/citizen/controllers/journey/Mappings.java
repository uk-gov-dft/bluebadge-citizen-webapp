package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;

@Getter
public class Mappings {

  public static final String URL_REMOVE_PART = "/remove";

  public static final String URL_ROOT = "/";
  public static final String URL_APPLICANT_TYPE = "/applicant";
  public static final String URL_APPLICANT_NAME = "/name";
  public static final String URL_RECEIVE_BENEFITS = "/benefits";
  public static final String URL_HIGHER_RATE_MOBILITY = "/higher-rate-mobility";
  public static final String URL_ELIGIBLE = "/eligible";
  public static final String URL_MAY_BE_ELIGIBLE = "/may-be-eligible";
  public static final String URL_HEALTH_CONDITIONS = "/health-conditions";
  public static final String URL_DECLARATIONS = "/apply-for-a-blue-badge/declaration";
  public static final String URL_BADGE_PAYMENT = "/badge-payment";
  public static final String URL_BADGE_PAYMENT_BYPASS = "/badge-payment-by-pass";
  public static final String URL_NOT_PAID = "/not-paid";
  public static final String URL_BADGE_PAYMENT_RETURN = "/badge-payment-return";
  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String URL_CHOOSE_YOUR_COUNCIL = "/choose-council";
  public static final String URL_YOUR_ISSUING_AUTHORITY = "/your-issuing-authority";
  public static final String URL_DIFFERENT_SERVICE_SIGNPOST = "/different-service-signpost";
  public static final String URL_DOB = "/date-of-birth";
  public static final String URL_CONTACT_DETAILS = "/contact-details";
  public static final String URL_GENDER = "/gender";
  public static final String URL_NINO = "/nino";
  public static final String URL_ENTER_ADDRESS = "/enter-address";
  public static final String URL_EXISTING_BADGE = "/existing-badge";
  public static final String URL_PROVE_BENEFIT = "/prove-benefit";
  public static final String URL_UPLOAD_BENEFIT = "/upload-benefit";
  public static final String URL_PROVE_IDENTITY = "/prove-identity";
  public static final String URL_PROVE_ADDRESS = "/prove-address";
  public static final String URL_PROVIDE_PHOTO = "/provide-photo";
  public static final String URL_UPLOAD_SUPPORTING_DOCUMENTS = "/upload-supporting-documents";

  // organisation Journey routes
  public static final String URL_ORGANISATION_CARE = "/organisation-care";
  public static final String URL_ORGANISATION_TRANSPORT = "/organisation-transport";
  public static final String URL_ORGANISATION_MAY_BE_ELIGIBLE = "/organisation-may-be-eligible";
  public static final String URL_ORGANISATION_NOT_ELIGIBLE = "/organisation-not-eligible";

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
  public static final String URL_COOKIES = "/cookies";
  public static final String URL_PRIVACY = "/privacy-notice";

  public static final String URL_MOBILITY_AID_LIST = "/list-mobility-aids";
  public static final String URL_MOBILITY_AID_ADD = "/add-mobility-aid";
  public static final String URL_TREATMENT_LIST = "/list-treatments";
  public static final String URL_TREATMENT_ADD = "/add-treatment";
  public static final String URL_HEALTHCARE_PROFESSIONALS_LIST = "/list-healthcare-professionals";
  public static final String URL_HEALTHCARE_PROFESSIONALS_ADD = "/add-healthcare-professional";

  public static final String URL_MEDICATION_LIST = "/list-medication";
  public static final String URL_MEDICATION_REMOVE = "/list-medication/remove";
  public static final String URL_MEDICATION_ADD = "/add-medication";
  public static final String URL_MEDICAL_EQUIPMENT = "/medical-equipment";

  // Walking difficulties route
  public static final String URL_WHAT_MAKES_WALKING_DIFFICULT = "/what-makes-walking-difficult";
  public static final String URL_WHERE_CAN_YOU_WALK = "/where-can-you-walk";
  public static final String URL_WALKING_TIME = "/walking-time";

  // Blind
  public static final String URL_REGISTERED = "/registered";
  public static final String URL_PERMISSION = "/permission";
  public static final String URL_REGISTERED_COUNCIL = "/registered-council";

  // Arms route
  public static final String URL_ARMS_HOW_OFTEN_DRIVE = "/how-often-drive";
  public static final String URL_ARMS_DRIVE_ADAPTED_VEHICLE = "/drive-adapted-vehicle";
  public static final String URL_ARMS_DIFFICULTY_PARKING_METERS = "/difficulty-parking-meters";

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
          .put(StepDefinition.PROVE_BENEFIT, URL_PROVE_BENEFIT)
          .put(StepDefinition.UPLOAD_BENEFIT, URL_UPLOAD_BENEFIT)
          .put(StepDefinition.PROVE_IDENTITY, URL_PROVE_IDENTITY)
          .put(StepDefinition.PROVIDE_PHOTO, URL_PROVIDE_PHOTO)
          .put(StepDefinition.PROVE_ADDRESS, URL_PROVE_ADDRESS)

          // ARMS Journey Mappings
          .put(StepDefinition.ARMS_HOW_OFTEN_DRIVE, URL_ARMS_HOW_OFTEN_DRIVE)
          .put(StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE, URL_ARMS_DRIVE_ADAPTED_VEHICLE)
          .put(StepDefinition.ARMS_DIFFICULTY_PARKING_METER, URL_ARMS_DIFFICULTY_PARKING_METERS)

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
          .put(StepDefinition.WALKING_TIME, URL_WALKING_TIME)
          .put(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS, URL_UPLOAD_SUPPORTING_DOCUMENTS)
          .put(StepDefinition.NOT_ELIGIBLE, URL_NOT_ELIGIBLE)
          .put(StepDefinition.EXISTING_BADGE, URL_EXISTING_BADGE)
          .put(StepDefinition.HEALTH_CONDITIONS, URL_HEALTH_CONDITIONS)
          .put(StepDefinition.WHAT_MAKES_WALKING_DIFFICULT, URL_WHAT_MAKES_WALKING_DIFFICULT)
          .put(StepDefinition.CHOOSE_COUNCIL, URL_CHOOSE_YOUR_COUNCIL)
          .put(StepDefinition.YOUR_ISSUING_AUTHORITY, URL_YOUR_ISSUING_AUTHORITY)
          .put(StepDefinition.DIFFERENT_SERVICE_SIGNPOST, URL_DIFFERENT_SERVICE_SIGNPOST)
          .put(StepDefinition.REGISTERED, URL_REGISTERED)
          .put(StepDefinition.PERMISSION, URL_PERMISSION)
          .put(StepDefinition.REGISTERED_COUNCIL, URL_REGISTERED_COUNCIL)
          .put(StepDefinition.DECLARATIONS, URL_DECLARATIONS)
          .put(StepDefinition.BADGE_PAYMENT_RETURN, URL_BADGE_PAYMENT_RETURN)
          .put(StepDefinition.NOT_PAID, URL_NOT_PAID)
          .put(StepDefinition.BADGE_PAYMENT, URL_BADGE_PAYMENT)
          .put(StepDefinition.SUBMITTED, URL_APPLICATION_SUBMITTED)
          .put(StepDefinition.MOBILITY_AID_LIST, URL_MOBILITY_AID_LIST)
          .put(StepDefinition.MOBILITY_AID_ADD, URL_MOBILITY_AID_ADD)
          .put(StepDefinition.TREATMENT_LIST, URL_TREATMENT_LIST)
          .put(StepDefinition.TREATMENT_ADD, URL_TREATMENT_ADD)
          .put(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST, URL_HEALTHCARE_PROFESSIONALS_LIST)
          .put(StepDefinition.HEALTHCARE_PROFESSIONALS_ADD, URL_HEALTHCARE_PROFESSIONALS_ADD)
          .put(StepDefinition.MEDICATION_LIST, URL_MEDICATION_LIST)
          .put(StepDefinition.MEDICATION_ADD, URL_MEDICATION_ADD)
          .put(StepDefinition.MEDICAL_EQUIPMENT, URL_MEDICAL_EQUIPMENT)

          // organisation mappings
          .put(StepDefinition.ORGANISATION_CARE, URL_ORGANISATION_CARE)
          .put(StepDefinition.ORGANISATION_TRANSPORT, URL_ORGANISATION_TRANSPORT)
          .put(StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE, URL_ORGANISATION_MAY_BE_ELIGIBLE)
          .put(StepDefinition.ORGANISATION_NOT_ELIGIBLE, URL_ORGANISATION_NOT_ELIGIBLE)
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
