package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class ChooseCouncilPage {

  public static final String PAGE_URL = "/choose-council";
  private static final String YOURSELF_LABEL = "your";
  private static final String SOMEONE_ELSE_LABEL = "their";

  public static final String PAGE_TITLE_YOURSELF = "Choose your local council";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Choose their local council";

  public static final String COUNCIL_INPUT = "councilShortCode";
  public static final String REGISTERED_COUNCIL_INPUT = "registeredCouncil";

  public static final String VALIDATION_MESSAGE_FOR_NO_OPTION = "Select the local council";

  public static final String PAGE_LABEL_1_YOURSELF =
      "You cannot get a Blue Badge if you live outside of the UK.";
  public static final String PAGE_LABEL_1_SOMEONE_ELSE =
      "They cannot get a Blue Badge if they live outside of the UK.";

  public static final String PAGE_LABEL_2 =
      "You'll need to apply through direct.gov if your council isn't listed.";
  public static final String PAGE_LABEL_3 = "Start typing and select your council";
  public static final String DO_NOT_KNOW_COUNCIL_LINK_TEXT = "You don't know their local council";
  public static final String DO_NOT_KNOW_COUNCIL_LINK =
      "https://www.gov.uk/blue-badge-scheme-information-council";

  public String getPageTitle(String applicant) {
    String pageTitle;
    if ("yourself".equals(applicant.toLowerCase())) {
      pageTitle = "Choose " + YOURSELF_LABEL + " local council";
    } else {
      pageTitle = "Choose " + SOMEONE_ELSE_LABEL + " local council";
    }

    return pageTitle;
  }
}
