package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class HealthConditionsPage {

  public static final String PAGE_URL = "/health-conditions";
  public static final String PAGE_TITLE = "Describe health conditions or disabilities";
  public static final String PAGE_HEADER =
      "Describe any health conditions or disabilities that affect your walking";
  public static final String PAGE_HEADER_WALKING =
      "Name any health conditions or disabilities that affect your walking";
  public static final String PAGE_HEADER_SOMEONE_ELSE =
      "Describe any health conditions or disabilities that affect their walking";
  public static final String PAGE_HEADER_SOMEONE_ELSE_WALKING =
      "Name any health conditions or disabilities that affect their walking";

  public static final String VALID_DESCRIPTION =
      "This is my sample description!!@£$%^&*()_+{};\"|\n <>?~±§1234567890-=[];'\\,./`. I'm happy";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_DESCRIPTION =
      "Description must be 9,000 characters or less";

  public static final String DESCRIPTION_ID = "descriptionOfConditions";
}
