package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class ContactDetailsPage {

  public static final String PAGE_URL = "/contact-details";
  public static final String PAGE_TITLE = "Enter contact details";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_PHONE_NUMBER =
      "Enter main contact number";
  public static final String VALIDATION_MESSAGE_FOR_CONTACT_FULL_NAME = "Enter contact\'s full name";

  public static final String VALIDATION_MESSAGE_FOR_INVALID_MAIN_PHONE_NUMBER = "Main contact number must be in the correct format";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_ALTERNATIVE_PHONE_NUMBER = "Alternative contact number must be in the correct format";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_EMAIL = "Enter a valid email address";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_FULLNAME = "Enter a valid full name";


  public static final String FULL_NAME = "fullName";
  public static final String PRIMARY_CONTACT_NUMBER = "primaryPhoneNumber";
  public static final String SECONDARY_CONTACT_NUMBER = "secondaryPhoneNumber";
  public static final String EMAIL_ADDRESS = "emailAddress";
}
