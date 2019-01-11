package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class AddTreatmentsPage {

  public static final String PAGE_URL = "/add-treatment";
  public static final String PAGE_TITLE = "Add treatment";

  public static final String TREATMENT_DESCRIPTION = "treatmentDescription";
  public static final String TREATMENT_WHEN = "treatmentWhen";

  public static final String TREATMENT_ADD_BUTTON = "button.continue";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_TREATMENT_DESCRIPTION =
      "Enter treatment description";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_TREATMENT_WHEN =
      "Enter when treatment was received";

  //More than 100 characters string
  public static final String INVALID_TEXT =
      "uytzg5m7dHQ2wQZz2X0miCt783jnyLnTVuA4BODpdmrx1V20aawmLyxFegM1J25t6vl1bdWdlKbhoUq09DsHQWBXuUvAS0uePri6p";

  public static final String VALID_DESCRIPTION = "This is a treatment for pain";
  public static final String VALID_WHEN = "6 Months ago";

  public static final String VALIDATION_MESSAGE_FOR_INVALID_DESCRIPTION =
      "Treatment description must be 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_WHEN =
      "Treatment received date must be 100 characters or less";
}
