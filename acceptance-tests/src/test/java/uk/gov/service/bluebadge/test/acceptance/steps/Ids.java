package uk.gov.service.bluebadge.test.acceptance.steps;

class Ids {

  class EleCheck {

    static final String MAIN_REASON_LIST = "mainReasonOption";

    static final String EQUIPMENT_LIST = "equipment";
    static final String MEDICAL_EQUIPMENT = "medicalEquipment";
    static final String HAS_RECEIVED_DLA = "receivedDlaOption";
    static final String NEVER_RECEIVED_DLA = "receivedDlaOption.NEVER_RECEIVED_DLA";
    static final String MOVING_POINTS = "movingAroundPoints";
    static final String PLANNING_POINTS = "planningJourneyOption";
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
    static final String ORGANISATION_CARES = "doesCare";
    static final String ORGANISATION_TRANSPORTS = "doesTransport";
  }

  class Person {
    static final String NAME = "fullName";
    static final String HAS_BIRTH_NAME_NO_OPTION = "hasBirthName.no";
    static final String DOB = "dateOfBirth";
    static final String DOB_DAY = "dateOfBirth.day";
    static final String DOB_MONTH = "dateOfBirth.month";
    static final String DOB_YEAR = "dateOfBirth.year";
    static final String GENDER = "gender";
    static final String GENDER_MALE = "gender.MALE";
    static final String GENDER_FEMALE = "gender.FEMALE";
    static final String GENDER_UNSPECIFIED = "gender.UNSPECIFIE";
    static final String NI = "nino";
    static final String NO_NI_LINK = "noNino";
    static final String SKIP_WITHOUT_NI = "skipNiLink";
  }

  class Walkd {
    static final String TREATMENT_ADD_CONFIRM_BUTTON = "button.add.treatment";
    static final String TREATMENT_HAS_TREATMENT_OPTION = "hasTreatment";
    // Id of link will be prefix + counter.  Counter starts at 1.
    static final String TREATMENT_REMOVE_LINK_PREFIX = "treatment.remove.link.";
    static final String TREATMENT_ADD_FIRST_LINK = "firstaddtreatmentlink";
    static final String TREATMENT_ADD_TREATMENT_DESCRIPTION = "treatmentDescription";
    static final String TREATMENT_ADD_TREATMENT_PAST_OPTION = "treatmentWhenType";
    static final String TREATMENT_ADD_TREATMENT_PAST_WHEN = "treatmentPastWhen";
    static final String TREATMENT_ADD_TREATMENT_ONGOING_OPTION = "treatmentWhenType.ONGOING";
    static final String TREATMENT_ADD_TREATMENT_ONGOING_FREQUENCY = "treatmentOngoingFrequency";
    static final String TREATMENT_ADD_TREATMENT_FUTURE_OPTION = "treatmentWhenType.FUTURE";
    static final String TREATMENT_ADD_TREATMENT_FUTURE_WHEN = "treatmentFutureWhen";
    static final String TREATMENT_ADD_TREATMENT_FUTURE_IMPROVE = "treatmentFutureImprove";

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

  class UploadSupportingDocuments {
    static final String UPLOAD_SUPPORTING_DOCUMENTS_YES = "hasDocuments.yes";
    static final String UPLOAD_SUPPORTING_DOCUMENTS_NO = "hasDocuments.no";
  }

  class RegisteredCouncil {
    static final String HAS_REGISTERED = "hasRegistered";
    static final String HAS_PERMISSION = "hasPermission";
    static final String HAS_REGISTERED_COUNCIL = "registeredCouncil";
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

  class Arms {
    static final String DIFFICULTY_PARKING_METERS_DESC = "parkingMetersDifficultyDescription";
    static final String IS_ADAPTED_VEHICLE_OPTION = "hasAdaptedVehicle";
    static final String ADAPTED_VEHICLE_DESCRIPTIOM = "adaptedVehicleDescription";
    static final String HOW_OFTEN_DRIVE = "howOftenDrive";
  }
}
