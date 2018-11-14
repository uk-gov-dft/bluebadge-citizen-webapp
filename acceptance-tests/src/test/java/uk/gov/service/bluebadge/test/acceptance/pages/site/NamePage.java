package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class NamePage {

  public static final String PAGE_URL = "/name";
  public static final String PAGE_TITLE_YOURSELF = "What's your name?";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "What's their name?";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_FULL_NAME = "Enter full name";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_FULL_NAME = "Enter a valid name";

  public static final String VALIDATION_MESSAGE_FOR_BIRTH_NAME_NO_OPTION =
      "Select whether birth name has changed";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_BIRTH_NAME = "Enter a valid birth name";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_BIRTH_NAME = "Enter full name at birth";

  public static final String NAME_BIRTH_NAME_OPTIONS = "hasBirthName";
}
