package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;

public class WalkingTimeControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  private static final String ERROR_URL = Mappings.URL_WALKING_TIME + RouteMaster.ERROR_SUFFIX;

  @Before
  public void setup() {
    WalkingTimeController controller = new WalkingTimeController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_TIME, EligibilityCodeField.WALKD, false);
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/walking-time"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getWalkingTimeForm()))
        .andExpect(model().attributeExists("walkingTimeOptions"));
  }

  @Test
  public void show_walkingTimeOptionsShouldBeSet() throws Exception {
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("walkingTimeOptions"));
  }

  @Test
  public void show_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues() throws Exception {
    WalkingTimeForm form =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.LESSMIN).build();

    journey.setFormForStep(form);
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/walking-time"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/walking-time")
                .param("walkingTime", "LESSMIN")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_WHERE_CAN_YOU_WALK));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    WalkingTimeForm form = WalkingTimeForm.builder().build();

    mockMvc
        .perform(
            post("/walking-time")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(flash().attribute("formRequest", form));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldHaveNotEmptyErrorMessage() throws Exception {
    mockMvc
        .perform(
            post("/walking-time")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "walkingTime", "NotNull"));
  }
}
