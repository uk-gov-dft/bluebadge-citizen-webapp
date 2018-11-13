package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class NinoPage {

  public static final String PAGE_URL = "/nino";
  public static final String PAGE_TITLE_YOURSELF = "What's your National Insurance number?";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "What's their National Insurance number?";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_NINO =
      "National Insurance number must be in the correct format";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_NINO =
      "National Insurance number must be in the correct format";
  public static final String INVALID_NINO = "1234";
  public static final String VALID_NINO = "SN123456C";
}
