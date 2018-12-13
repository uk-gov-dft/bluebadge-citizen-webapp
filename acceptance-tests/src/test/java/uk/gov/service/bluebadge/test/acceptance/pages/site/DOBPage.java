package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class DOBPage {

  public static final String PAGE_URL = "/date-of-birth";
  public static final String PAGE_TITLE_YOURSELF = "What's your date of birth";
  public static final String PAGE_HEADING = "What's your date of birth?";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "What's their date of birth";
  public static final String PAGE_HEADING_SOMEONE_ELSE = "What's their date of birth?";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_DOB =
      "Enter a valid date of birth in DD MM YYYY format";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_DOB =
      "Enter a valid date of birth in DD MM YYYY format";

  public static final String VALIDATION_MESSAGE_FOR_DOB_IN_FUTURE =
      "Date of birth must be a date in the past";
}
