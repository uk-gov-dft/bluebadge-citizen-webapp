package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class FindCouncilPage {

  public static final String PAGE_URL = "/find-council";
  public static final String PAGE_TITLE_YOURSELF = "Find your local council";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Find their local council";

  public static final String POSTCODE_INPUT = "postcode.field";
  public static final String CHOOSE_YOUR_COUNCIL_LINK = "chooseYourCouncil";

  public static final String PAGE_LABEL_1_YOURSELF =
      "You cannot get a Blue Badge if you live outside of the UK.";
  public static final String PAGE_LABEL_1_SOMEONE_ELSE =
      "They cannot get a Blue Badge if they live outside of the UK.";

  public static final String VALIDATION_MESSAGE_EMPTY_POSTCODE = "Enter a postcode";
  public static final String VALIDATION_MESSAGE_INVALID_POSTCODE = "Enter a valid postcode";
}
