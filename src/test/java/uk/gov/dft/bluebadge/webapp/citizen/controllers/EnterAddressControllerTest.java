package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;

public class EnterAddressControllerTest {

  private static final String BUILDING_AND_STREET_MIN = "H";
  private static final String TOWN_OR_CITY_MIN = "A";
  private static final String BUILDING_AND_STREET = "High Street 1";
  private static final String OPTIONAL_ADDRESS = "Optional address";
  private static final String TOWN_OR_CITY = "A random place in the world";
  private static final String POSTCODE = "UB6 0RQ";
  private static final String POSTCODE_WRONG = "WRONG";
  private static final String BUILDING_AND_STREET_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String TOWN_OR_CITY_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String OPTIONAL_ADDRESS_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String BUILDING_AND_STREET_OVER_MAX = BUILDING_AND_STREET_MAX + "12345";
  private static final String TOWN_OR_CITY_OVER_MAX = TOWN_OR_CITY_MAX + "12345";
  private static final String OPTIONAL_ADDRESS_OVER_MAX = OPTIONAL_ADDRESS_MAX + "12345";

  private static final String ERROR_URL = Mappings.URL_ENTER_ADDRESS + RouteMaster.ERROR_SUFFIX;
  private static final String SUCCESS_URL = Mappings.URL_CONTACT_DETAILS;
  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    EnterAddressController controller =
        new EnterAddressController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.NINO);
  }

  @Test
  public void show_ShouldDisplayEnterAddressTemplate() throws Exception {
    EnterAddressForm formRequest = EnterAddressForm.builder().build();

    mockMvc
        .perform(get("/enter-address").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("enter-address"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/enter-address"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenEmptyValues_thenShouldDisplayNotBlankValidationErrors() throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", "")
                .param("townOrCity", "")
                .param("postcode", "")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "buildingAndStreet", "NotBlank"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "townOrCity", "NotBlank"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "NotBlank"));
  }

  @Test
  public void submit_givenMinimumValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_MIN)
                .param("townOrCity", TOWN_OR_CITY_MIN)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET)
                .param("townOrCity", TOWN_OR_CITY)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidFormWithMaxValues_thenShouldDisplayRedirectToSuccess()
      throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_MAX)
                .param("townOrCity", TOWN_OR_CITY_MAX)
                .param("optionalAddress", OPTIONAL_ADDRESS_MAX)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidFormWithOverMaxValues_thenShouldDisplaySizeValidationErrors()
      throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_OVER_MAX)
                .param("townOrCity", TOWN_OR_CITY_OVER_MAX)
                .param("optionalAddress", OPTIONAL_ADDRESS_OVER_MAX)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "buildingAndStreet", "Size"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "optionalAddress", "Size"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode("townOrCity", "Size"));
  }

  @Test
  public void submit_givenValidFormExceptPostCode_thenShouldDisplayPatternValidationError()
      throws Exception {

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET)
                .param("townOrCity", TOWN_OR_CITY)
                .param("optionalAddress", OPTIONAL_ADDRESS)
                .param("postcode", POSTCODE_WRONG)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "Pattern"));
  }
}
