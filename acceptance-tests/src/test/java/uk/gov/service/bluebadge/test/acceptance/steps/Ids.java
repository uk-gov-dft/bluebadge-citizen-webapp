package uk.gov.service.bluebadge.test.acceptance.steps;

class Ids {
  class Preamble {
    static final String APPLICANT_TYPE_OPTION_LIST = "applicantType";
    static final String APPLICANT_TYPE_YOURSELF_OPTION = APPLICANT_TYPE_OPTION_LIST + ".YOURSELF";
    static final String APPLICANT_TYPE_SOMELSE_OPTION =
        APPLICANT_TYPE_OPTION_LIST + ".SOMEONE_ELSE";
    static final String COUNCIL_INPUT = "councilShortCode";
  }

  class EleCheck {
    static final String BENEFIT_RECEIVED_LIST = "benefitType";
    static final String MAIN_REASON_LIST = "mainReasonOption";
    static final String WALKING_DIFFICULTY_LIST = "walkingDifficulty";
    static final String WHAT_WALKING_DIFFICULTY_LIST = "whatWalkingDifficulties";
    static final String HAS_RECEIVED_DLA = "receivedDlaOption.HAS_RECEIVED_DLA";
    static final String NEVER_RECEIVED_DLA = "receivedDlaOption.NEVER_RECEIVED_DLA";
    static final String MOVING_POINTS = "movingAroundPoints.MOVING_POINTS";
    static final String PLANNING_POINTS = "planningJourneyOption.PLANNING_POINTS";
    static final String GENDER_MALE = "gender.MALE";
    static final String GENDER_FEMALE = "gender.FEMALE";
    static final String GENDER_UNSPECIFIED = "gender.UNSPECIFIE";
    static final String NI = "nino";
    static final String NO_NI_TEXT = "You don't have a National Insurance number";
    static final String NO_NI_LINK_TEXT = "Continue without a National Insurance number.";
    static final String FEEDBACK_URL = "https://www.smartsurvey.co.uk/s/WJLXY/";
    static final String APPLY_IN_WELSH_EXTERNAL_URL =
        "https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here";
    static final String GOOGLE_ANALYTICS_TAG = "UA-124760983-5";
    static final String PLACE_CAN_WALK = "destinationToHome";
    static final String TIME_TO_DESTINATION = "timeToDestination";
    static final String RECEIVED_COMPENSATION = "hasReceivedCompensation";
    static final String HAS_DISABILITY = "hasDisability";
    static final String HAS_MENTAL_DISORDER = "hasMentalDisorder";
    static final String AWARDED_HIGHER_RATE_MOBILITY = "awardedHigherRateMobility";
  }

  class Person {
    static final String NAME = "fullName";
    static final String HAS_BIRTH_NAME_NO_OPTION = "hasBirthName.no";
    static final String DOB_DAY = "dateOfBirth.day";
    static final String DOB_MONTH = "dateOfBirth.month";
    static final String DOB_YEAR = "dateOfBirth.year";
    static final String GENDER_MALE = "gender.MALE";
    static final String GENDER_FEMALE = "gender.FEMALE";
    static final String GENDER_UNSPECIFIED = "gender.UNSPECIFIE";
    static final String NI = "nino";
    static final String NO_NI_TEXT = "You don't have a National Insurance number";
    static final String NO_NI_LINK_TEXT = "Continue without a National Insurance number.";
  }

  class Contact {
    static final String FULL_NAME = "fullName";
    static final String PRIMARY_CONTACT_NUMBER = "primaryPhoneNumber";
    static final String SECONDARY_CONTACT_NUMBER = "secondaryPhoneNumber";
    static final String EMAIL_ADDRESS = "emailAddress";
  }

  class Walkd {
    static final String TREATMENT_ADD_CONFIRM_BUTTON = "button.add.treatment";
    static final String TREATMENT_HAS_TREATMENT_OPTION = "hasTreatment";
    // Id of link will be prefix + counter.  Counter starts at 1.
    static final String TREATMENT_REMOVE_LINK_PREFIX = "treatment.remove.link.";
    static final String TREATMENT_ADD_FIRST_LINK = "firstaddtreatmentlink";
    static final String TREATMENT_ADD_TREATMENT_DESCRIPTION = "treatmentDescription";
    static final String TREATMENT_ADD_TREATMENT_WHEN = "treatmentWhen";
    static final String MOBILITY_AID_OPTION = "hasWalkingAid";
    // Id of link will be prefix + counter.  Counter starts at 1.
    static final String MOBILITY_AID_REMOVE_LINK_PREFIX = "mobility.aid.remove.link.";
    static final String MOBILITY_AID_ADD_FIRST_LINK = "addmobilityaidlink";
    static final String MOBILITY_AID_TYPE_WHEELCHAIR = "aidTypeWHEELCHAIR";
    static final String MOBILITY_AID_ADD_PROVIDED_CODE_PRESCRIBE = "howProvidedCodeField.PRESCRIBE";
    static final String MOBILITY_AID_ADD_USAGE = "usage";
    static final String MOBILITY_AID_ADD_CONFIRM_BUTTON = "button.continue";

    static final String WALKING_TIME = "walkingTime";

    // Medication stuff
    static final String MEDICATION_ADD_FIRST_LINK = "firstaddmedicationlink";
    static final String MEDICATION_HAS_MEDICATION_OPTION = "hasMedication";
    static final String MEDICATION_ADD_MEDICATION_DESCRIPTION = "name";
    static final String MEDICATION_PRESCRIBED_OPTION = "prescribed";
    static final String MEDICATION_DOSAGE_TEXT = "dosage";
    static final String MEDICATION_ADD_CONFIRM_BUTTON = "button.add.medication";
    static final String MEDICATION_FREQUENCY_TEXT = "frequency";
  }

  class Eligibility {
    static final String HEALTHCARE_PRO_ADD_CONFIRM_BUTTON = "button.add.healthcare.professional";
    static final String HEALTHCARE_PRO_HAS_OPTION = "hasHealthcareProfessional";
    // Id of link will be prefix + counter.  Counter starts at 1.
    static final String HEALTHCARE_PRO_REMOVE_LINK_PREFIX = "healthcare.professional.remove.link.";
    static final String HEALTHCARE_PRO_ADD_FIRST_LINK = "firstaddhealthcareprofessionallink";
    static final String HEALTHCARE_PRO_ADD_DESCRIPTION = "healthcareProfessionalName";
    static final String HEALTHCARE_PRO_ADD_LOCATION = "healthcareProfessionalLocation";
  }
}
