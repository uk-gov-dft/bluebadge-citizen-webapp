package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class AddHealthcareProfessionalPage {

  public static final String PAGE_URL = "/add-healthcare-professional";
  public static final String PAGE_TITLE = "Add healthcare professional";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Add healthcare professional";

  public static final String NAME = "healthcareProfessionalName";
  public static final String Location = "healthcareProfessionalLocation";

  public static final String HC_PROFESSIONAL_ADD_BUTTON = "button.continue";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_NAME =
      "Enter a healthcare professional name that is 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_LOCATION =
      "Enter a hospital or health centre name that is 100 characters or less";

  //More than 100 characters string
  public static final String INVALID_TEXT =
      "uytzg5m7dHQ2wQZz2X0miCt783jnyLnTVuA4BODpdmrx1V20aawmLyxFegM1J25t6vl1bdWdlKbhoUq09DsHQWBXuUvAS0uePri6p";

  public static final String VALID_NAME = "Mr.Anderson Smith";
  public static final String VALID_LOCATION = "Wellness Clinic, Wakefield, WF1 3QB";

  public static final String VALIDATION_MESSAGE_FOR_INVALID_NAME =
      "Enter a healthcare professional name that is 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_LOCATION =
      "Enter a hospital or health centre name that is 100 characters or less";
}
