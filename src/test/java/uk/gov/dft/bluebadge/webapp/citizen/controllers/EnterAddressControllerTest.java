package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;

public class EnterAddressControllerTest {

  public static final String BUILDING_AND_STREET_MIN = "H";
  public static final String TOWN_OR_CITY_MIN = "A";
  public static final String BUILDING_AND_STREET = "High Street 1";
  public static final String OPTIONAL_ADDRESS = "Optional address";
  public static final String TOWN_OR_CITY = "A random place in the world";
  public static final String POSTCODE = "UB6 0RQ";
  public static final String POSTCODE_WRONG = "WRONG";
  public static final String BUILDING_AND_STREET_MAX = StringUtils.leftPad("a", 100, 'b');
  public static final String TOWN_OR_CITY_MAX = StringUtils.leftPad("a", 100, 'b');
  public static final String OPTIONAL_ADDRESS_MAX = StringUtils.leftPad("a", 100, 'b');
  public static final String BUILDING_AND_STREET_OVER_MAX = BUILDING_AND_STREET_MAX + "12345";
  public static final String TOWN_OR_CITY_OVER_MAX = TOWN_OR_CITY_MAX + "12345";
  public static final String OPTIONAL_ADDRESS_OVER_MAX = OPTIONAL_ADDRESS_MAX + "12345";

  private MockMvc mockMvc;
  private EnterAddressController controller;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new EnterAddressController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new Journey();
    journey.setApplicantForm(
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.name()).build());
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
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/enter-address"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenEmptyValues_thenShouldDisplayNotBlankValidationErrors() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", "")
                .param("townOrCity", "")
                .param("postcode", "")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("enter-address"))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "buildingAndStreet", "NotBlank"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "townOrCity", "NotBlank"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "postcode", "NotBlank"))
        .andExpect(model().errorCount(3));
  }

  @Test
  public void submit_givenMinimumValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_MIN)
                .param("townOrCity", TOWN_OR_CITY_MIN)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET)
                .param("townOrCity", TOWN_OR_CITY)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidFormWithMaxValues_thenShouldDisplayRedirectToSuccess()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_MAX)
                .param("townOrCity", TOWN_OR_CITY_MAX)
                .param("optionalAddress", OPTIONAL_ADDRESS_MAX)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidFormWithOverMaxValues_thenShouldDisplaySizeValidationErrors()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET_OVER_MAX)
                .param("townOrCity", TOWN_OR_CITY_OVER_MAX)
                .param("optionalAddress", OPTIONAL_ADDRESS_OVER_MAX)
                .param("postcode", POSTCODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("enter-address"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "buildingAndStreet", "Size"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "optionalAddress", "Size"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "townOrCity", "Size"))
        .andExpect(model().errorCount(3));
  }

  @Test
  public void submit_givenValidFormExceptPostCode_thenShouldDisplayPatternValidationError()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(EnterAddressForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/enter-address")
                .param("buildingAndStreet", BUILDING_AND_STREET)
                .param("townOrCity", TOWN_OR_CITY)
                .param("optionalAddress", OPTIONAL_ADDRESS)
                .param("postcode", POSTCODE_WRONG)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("enter-address"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "postcode", "Pattern"))
        .andExpect(model().errorCount(1));
  }
}
