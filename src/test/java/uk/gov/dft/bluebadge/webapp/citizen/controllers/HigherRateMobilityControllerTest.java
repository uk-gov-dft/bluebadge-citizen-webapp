package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class HigherRateMobilityControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    HigherRateMobilityController controller =
        new HigherRateMobilityController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.HIGHER_RATE_MOBILITY, EligibilityCodeField.DLA);
  }

  @Test
  public void show_ShouldDisplayHigherRateMobilityTemplate() throws Exception {

    mockMvc
        .perform(get("/higher-rate-mobility").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("higher-rate-mobility"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getHightRateMobilityForm()))
        .andExpect(model().attribute("options", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/higher-rate-mobility"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenYesOption_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/higher-rate-mobility")
                .param("awardedHigherRateMobility", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ELIGIBLE));
  }

  @Test
  public void submit_givenNoOption_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/higher-rate-mobility")
                .param("awardedHigherRateMobility", "false")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_MAIN_REASON));
  }

  @Test
  public void submit_whenMissingHigherRateMobilityAnswer_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/higher-rate-mobility").sessionAttr("JOURNEY", journey))
        .andExpect(redirectedUrl(Mappings.URL_HIGHER_RATE_MOBILITY + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "awardedHigherRateMobility", "NotNull"));
  }
}
