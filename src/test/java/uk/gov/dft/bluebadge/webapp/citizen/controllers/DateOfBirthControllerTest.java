package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class DateOfBirthControllerTest {

  public static final String URL_DATE_OF_BIRTH = "/date-of-birth";
  public static final String VIEW_DATE_OF_BIRTH = "date-of-birth";
  private MockMvc mockMvc;
  private DateOfBirthController controller;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new DateOfBirthController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDateOfBirthTemplate() throws Exception {

    // A pre-set up journey
    Journey journey = JourneyFixture.getDefaultJourney();

    mockMvc
        .perform(get(URL_DATE_OF_BIRTH).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("date-of-birth"))
        .andExpect(model().attribute("formRequest", journey.getDateOfBirthForm()));
  }

  @Test
  public void show_givenMissingJourney_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get(URL_DATE_OF_BIRTH))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(controller)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("year", "1990")
                .param("month", "1")
                .param("day", "2")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingDay_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("year", "1990")
                .param("month", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_DATE_OF_BIRTH))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "pastDate", "AssertTrue"));
  }

  @Test
  public void submit_whenMissingMonth_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("year", "1990")
                .param("day", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_DATE_OF_BIRTH))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "pastDate", "AssertTrue"));
  }

  @Test
  public void submit_whenMissingYear_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("month", "2")
                .param("day", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_DATE_OF_BIRTH))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "pastDate", "AssertTrue"));
  }

  @Test
  public void submit_givenFutureDate_ThenShouldHaveValidationError() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(controller)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("year", "2500")
                .param("month", "1")
                .param("day", "2")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_DATE_OF_BIRTH))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "pastDate", "AssertTrue"));
  }
}
