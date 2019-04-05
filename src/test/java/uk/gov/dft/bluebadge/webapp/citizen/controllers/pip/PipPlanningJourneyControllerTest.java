package uk.gov.dft.bluebadge.webapp.citizen.controllers.pip;

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

public class PipPlanningJourneyControllerTest {
  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    PipPlanningJourneyController controller =
        new PipPlanningJourneyController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PIP_PLANNING_JOURNEY, EligibilityCodeField.PIP);
  }

  @Test
  public void show_ShouldDisplayPlanningJourneyTemplate() throws Exception {

    mockMvc
        .perform(get("/planning-and-following").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("pip/planning-journey"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getPipPlanningJourneyForm()))
        .andExpect(model().attribute("formOptions", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/planning-and-following"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/planning-and-following")
                .param("planningJourneyOption", "PLANNING_POINTS_8")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PIP_RECEIVED_DLA));
  }

  @Test
  public void submit_whenMissingPlanningJourneyAnswer_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/planning-and-following").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PIP_PLANNING_JOURNEY + RouteMaster.ERROR_SUFFIX));
  }
}
