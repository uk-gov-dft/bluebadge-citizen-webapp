package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

public class MainReasonControllerTest {

  private MockMvc mockMvc;

  private Journey journey;

  @Before
  public void setup() {
    MainReasonController controller = new MainReasonController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.MAIN_REASON, EligibilityCodeField.CHILDBULK);
  }

  @Test
  public void show_ShouldDisplayMainReasonTemplate() throws Exception {

    MainReasonForm form = journey.getFormForStep(StepDefinition.MAIN_REASON);
    mockMvc
        .perform(get("/main-reason").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/main-reason"))
        .andExpect(model().attribute("formRequest", form))
        .andExpect(model().attribute("formOptions", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/main-reason"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/main-reason").param("mainReasonOption", "WALKD").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_WALKING_DIFFICULTY));
  }

  @Test
  public void submit_whenMissingMainReasonAnswer_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post("/main-reason").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_MAIN_REASON + RouteMaster.ERROR_SUFFIX));
  }
}
