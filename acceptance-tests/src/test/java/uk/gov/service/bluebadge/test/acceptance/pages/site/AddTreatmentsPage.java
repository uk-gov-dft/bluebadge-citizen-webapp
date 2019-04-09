package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class AddTreatmentsPage {

  public static final String PAGE_URL = "/add-treatment";
  public static final String PAGE_TITLE = "Add treatment";

  public static final String TREATMENT_DESCRIPTION = "treatmentDescription";

  public static final String TREATMENT_PAST_OPTION = "treatmentWhenType";
  public static final String TREATMENT_PAST_WHEN = "treatmentPastWhen";
  public static final String TREATMENT_ONGOING_OPTION = "treatmentWhenType.ONGOING";
  public static final String TREATMENT_ONGOING_FREQUENCY = "treatmentOngoingFrequency";
  public static final String TREATMENT_FUTURE_OPTION = "treatmentWhenType.FUTURE";
  public static final String TREATMENT_FUTURE_WHEN = "treatmentFutureWhen";
  public static final String TREATMENT_FUTURE_IMPROVE = "treatmentFutureImprove";

  public static final String TREATMENT_ADD_BUTTON = "button.continue";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_TREATMENT_DESCRIPTION =
      "Enter treatment description";
  public static final String VALIDATION_MESSAGE_FOR_NULL_WHEN_TYPE = "Select date of treatment";

  //More than 100 characters string
  public static final String INVALID_TEXT =
      "uytzg5m7dHQ2wQZz2X0miCt783jnyLnTVuA4BODpdmrx1V20aawmLyxFegM1J25t6vl1bdWdlKbhoUq09DsHQWBXuUvAS0uePri6p";

  public static final String VALID_DESCRIPTION = "This is a treatment for pain";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_DESCRIPTION =
      "Treatment description must be 100 characters or less";

  public static final String VALID_WHEN = "6 Months ago";

  public static final String VALIDATION_MESSAGE_FOR_NULL_PAST_WHEN = "Enter treatment date";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_PAST_WHEN =
      "Treatment date must be 100 characters or less";

  public static final String VALIDATION_MESSAGE_FOR_NULL_ONGOING_FREQUENCY =
      "Enter treatment frequency";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_ONGOING_FREQUENCY =
      "Treatment frequency must be 90 characters or less";

  public static final String VALIDATION_MESSAGE_FOR_NULL_FUTURE_WHEN = "Enter treatment date";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_FUTURE_WHEN =
      "Treatment date must be 35 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_NULL_FUTURE_IMPROVE =
      "Enter whether condition expected to improve";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_FUTURE_IMPROVE =
      "Whether condition expected to improve must be 25 characters or less";
}
