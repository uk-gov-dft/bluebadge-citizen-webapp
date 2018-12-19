package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class AddMobilityAidsPage {

  public static final String PAGE_URL = "/add-mobility-aid";
  public static final String PAGE_TITLE = "Add mobility aid or support";

  public static final String MOBILITY_AID_TYPE = "aidType";
  public static final String MOBILITY_AID_ADD_PROVIDED_CODE_PRESCRIBE = "howProvidedCodeField";
  public static final String MOBILITY_AID_ADD_USAGE = "usage";
  public static final String MOBILITY_AID_ADD_CONFIRM_BUTTON = "button.continue";

  public static final String VALIDATION_MESSAGE_FOR_NO_TYPE =
      "Enter name of mobility aid or support";
  public static final String VALIDATION_MESSAGE_FOR_NO_HOW_USED =
      "Enter how mobility aid or support is used";
  public static final String VALIDATION_MESSAGE_FOR_NO_HOW_PROVIDED =
      "Select how this mobility aid or support was provided";
}
