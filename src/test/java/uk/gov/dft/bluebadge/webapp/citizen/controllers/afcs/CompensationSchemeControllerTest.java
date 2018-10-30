package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.AFCS_COMPENSATION_SCHEME;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;

public class CompensationSchemeControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    CompensationSchemeController controller = new CompensationSchemeController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            AFCS_COMPENSATION_SCHEME, EligibilityCodeField.AFRFCS);
  }

  @Test
  public void show_ShouldDisplayCompensationScheme_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.afcs.compensationSchemePage.title").withYesNoOptions();

    mockMvc
        .perform(get("/lump-sum").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("afcs/compensation-scheme"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getCompensationSchemeForm()))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {

    mockMvc
        .perform(post("/lump-sum").param("hasReceivedCompensation", ""))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_AFCS_COMPENSATION_SCHEME + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/lump-sum"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
