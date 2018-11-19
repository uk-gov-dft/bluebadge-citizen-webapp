package uk.gov.dft.bluebadge.webapp.citizen.controllers.arms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class DriveAdaptedVehicleControllerTest {
  private MockMvc mockMvc;
  private Journey journey;

  private static final String SUCCESS_URL = Mappings.URL_ARMS_DIFFICULTY_PARKING_METERS;
  private static final String TEST_URL = "/drive-adapted-vehicle";

  @Before
  public void setup() {
    DriveAdaptedVehicleController controller = new DriveAdaptedVehicleController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE, EligibilityCodeField.ARMS);
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get(TEST_URL).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("arms/drive-adapted-vehicle"));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get(TEST_URL))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(post(TEST_URL).param("hasAdaptedVehicle", "false").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_whenMissingAnswer_ThenShouldHaveValidationError() throws Exception {
    // If true, should have description
    mockMvc
        .perform(post(TEST_URL).param("hasAdaptedVehicle", "true").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(TEST_URL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "adaptedVehicleDescription", "NotBlank"));
  }
}
