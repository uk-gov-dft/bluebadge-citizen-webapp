package uk.gov.service.bluebadge.test.acceptance.pages.site;

public class EnterAddressPage {

  public static final String PAGE_URL = "/enter-address";
  public static final String PAGE_TITLE_YOURSELF = "Enter your address";
  public static final String PAGE_TITLE_SOMEONE_ELSE = "Enter their address";

  public static final String VALIDATION_MESSAGE_FOR_EMPTY_BUILDING_AND_STREET =
      "Enter a building and street";
  public static final String VALIDATION_MESSAGE_FOR_EMPTY_TOWN_CITY = "Enter a town or city";
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
