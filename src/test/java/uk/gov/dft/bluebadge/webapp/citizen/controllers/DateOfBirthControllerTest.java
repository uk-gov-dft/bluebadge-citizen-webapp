package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;

public class DateOfBirthControllerTest {

  private static final String URL_DATE_OF_BIRTH = "/date-of-birth";
  private static final String ERROR_URL = URL_DATE_OF_BIRTH + RouteMaster.ERROR_SUFFIX;
  private MockMvc mockMvc;

  @Before
  public void setup() {
    DateOfBirthController controller = new DateOfBirthController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDateOfBirthTemplate() throws Exception {

    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DOB);

    mockMvc
        .perform(get(URL_DATE_OF_BIRTH).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("date-of-birth"))
        .andExpect(
            model()
                .attribute(
                    "formRequest", (DateOfBirthForm) journey.getFormForStep(StepDefinition.DOB)));
  }

  @Test
  public void show_givenMissingJourney_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get(URL_DATE_OF_BIRTH))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DOB);

    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.year", "1990")
                .param("dateOfBirth.month", "1")
                .param("dateOfBirth.day", "2")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_GENDER));
  }

  @Test
  public void submit_whenMissingDay_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.year", "1990")
                .param("dateOfBirth.month", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "dateOfBirth", "ValidCompoundDate"));
  }

  @Test
  public void submit_whenMissingMonth_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.year", "1990")
                .param("dateOfBirth.day", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "dateOfBirth", "ValidCompoundDate"));
  }

  @Test
  public void submit_whenMissingYear_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.month", "2")
                .param("dateOfBirth.day", "1")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "dateOfBirth", "ValidCompoundDate"));
  }

  @Test
  public void submit_givenFutureDate_ThenShouldHaveValidationError() throws Exception {

    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.year", "2500")
                .param("dateOfBirth.month", "1")
                .param("dateOfBirth.day", "2")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "dateOfBirth", "PastCompoundDate"));
  }

  @Test
  public void submit_givenInvalidDate_ThenShouldHaveValidationError() throws Exception {

    mockMvc
        .perform(
            post(URL_DATE_OF_BIRTH)
                .param("dateOfBirth.year", "1967")
                .param("dateOfBirth.month", "1")
                .param("dateOfBirth.day", "32")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "dateOfBirth", "ValidCompoundDate"));
  }
}
