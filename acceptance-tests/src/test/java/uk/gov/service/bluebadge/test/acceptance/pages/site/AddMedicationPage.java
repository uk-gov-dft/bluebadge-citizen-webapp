package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class AddMedicationPage {

  public static final String PAGE_URL = "/add-medication";
  public static final String PAGE_TITLE = "Do you take any medication?";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Do they take any medication?";

  public static final String NAME = "name";
  public static final String PRESCRIBED = "prescribed";
  public static final String PRESCRIBED_NO = "prescribed.no";
  public static final String DOSAGE = "dosage";
  public static final String FREQUENCY  = "frequency";

  public static final String MEDICATION_ADD_BUTTON = "button.continue";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_DOSAGE = "Enter a dosage that is 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_PRESCRIBED = "Select whether this has been prescribed";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_NAME = "Enter a name that is 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_FREQUENCY = "Enter a frequency that is 100 characters or less";

  //More than 100 characters string
  public static final String INVALID_TEXT =
          "uytzg5m7dHQ2wQZz2X0miCt783jnyLnTVuA4BODpdmrx1V20aawmLyxFegM1J25t6vl1bdWdlKbhoUq09DsHQWBXuUvAS0uePri6p";

  public static final String VALID_NAME = "Amoxillin 500mg";
  public static final String VALID_DOSAGE = "2 tabs";
  public static final String VALID_FREQUENCY = "3 times a day";


  public static final String VALIDATION_MESSAGE_FOR_INVALID_DESCRIPTION = "Treatment description must be 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_WHEN = "Treatment received date must be 100 characters or less";


}
