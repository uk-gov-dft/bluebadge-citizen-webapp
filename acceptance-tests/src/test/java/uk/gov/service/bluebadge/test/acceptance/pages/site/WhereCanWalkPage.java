package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class WhereCanWalkPage {

  public static final String PAGE_URL = "/where-can-you-walk";
  public static final String PAGE_TITLE = "Where can you walk to?";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Where can they walk to?";

  public static final String PAGE_HEADING = "Where can you walk to?";
  public static final String PAGE_HEADING_SOMEONE_ELSE = "Where can they walk to?";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_PLACE = "Enter a place";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_TIME = "Enter how long it takes";

  public static final String VALIDATION_MESSAGE_FOR_INVALID_PLACE =
      "Place must be 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_TIME =
      "How long it takes must be 100 characters or less";

  //More than 100 Characters strings
  public static final String INVALID_PLACE =
      "N2XIbSLm9nIgezFRm8jbpvIbbGZsTKKN7j7akp6cbWswL85RFMYGwttbhtB4QKEkVsXhFJHN73L9bema5LMWl6RpSuwnOZLWroFdt";
  public static final String INVALID_TIME =
      "N2XIbSLm9nIgezFRm8jbpvIbbGZsTKKN7j7akp6cbWswL85RFMYGwttbhtB4QKEkVsXhFJHN73L9bema5LMWl6RpSuwnOZLWroFdt";

  public static final String VALID_PLACE = "From home to shops";
  public static final String VALID_TIME = "20 Minutes";

  public static final String WHERE_PLACE = "destinationToHome";
  public static final String WHERE_TIME = "timeToDestination";
}
