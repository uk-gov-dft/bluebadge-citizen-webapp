package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class ContactDetailsPage {

  public static final String PAGE_URL = "/contact-details";
  public static final String PAGE_TITLE = "Enter contact details";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_PHONE_NUMBER =
      "Enter main contact number";
  public static final String VALIDATION_MESSAGE_FOR_CONTACT_FULL_NAME = "Enter contact\'s full name";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_POSTCODE = "Enter a postcode";

  public static final String VALIDATION_MESSAGE_FOR_GT100_BUILDING_AND_STREET =
      "Building and street must be 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_GT100_TOWN_CITY =
      "Town or city must be 100 characters or less";
  public static final String VALIDATION_MESSAGE_FOR_INVALID_POSTCODE = "Enter a valid postcode";

  public static final String GREATER_THAN_100_CHARACTERS =
      "fixq1hBk4wUUp0vKu2TzMwH9KeGgdJ2UufcCXMmlc8xVwsD5Qle6yWbFKWp1eMbeYX8GylbfPptiHANOuwO8sf9A20nKHph7U8T5U";
  public static final String VALID_BUILDING_STREET = "65 Basil Chambers";
  public static final String VALID_TOWN = "Manchester";
  public static final String VALID_POSTCODE = "M4 1FS";
  public static final String INVALID_POSTCODE = "1234";
}
