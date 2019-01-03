package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class SubmittedControllerTest {

  private MockMvc mockMvc;

  @Before
  public void setup() {
    SubmittedController controller = new SubmittedController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showSubmitted_ShouldDisplaySubmittedTemplate_WhenThereIsAJourney() throws Exception {
    Journey journey = new JourneyBuilder().forYou().build();

    mockMvc
        .perform(get("/application-submitted").sessionAttr("JOURNEY", journey))
        .andExpect(view().name("application-end/submitted"))
        .andExpect(model().attribute("localAuthority", journey.getLocalAuthority()))
        .andExpect(request().sessionAttribute("JOURNEY", Matchers.nullValue()));
  }

  @Test
  public void showSubmitted_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/application-submitted"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void showDeclaration_givenInvalidState_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/application-submitted"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
