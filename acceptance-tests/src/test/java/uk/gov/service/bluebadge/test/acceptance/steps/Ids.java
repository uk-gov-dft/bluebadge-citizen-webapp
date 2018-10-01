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
  }

  class Person {
    static final String NAME = "fullName";
    static final String HAS_BIRTH_NAME_NO_OPTION = "hasBirthName.no";
  }
}
