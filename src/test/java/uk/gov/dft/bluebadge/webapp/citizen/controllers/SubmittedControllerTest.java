package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture.Values.EMAIL_ADDRESS;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

public class SubmittedControllerTest {

  private MockMvc mockMvc;
  @Mock RedirectVersionCookieManager mockCookieManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SubmittedController controller =
        new SubmittedController(RouteMasterFixture.routeMaster(), mockCookieManager);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDisplaySubmittedTemplate_WhenThereIsAJourney() throws Exception {
    Journey journey = new JourneyBuilder().forYou().build();

    mockMvc
        .perform(get("/application-submitted").sessionAttr("JOURNEY", journey))
        .andExpect(view().name("application-end/submitted"))
        .andExpect(model().attribute("JOURNEY", journey))
        .andExpect(model().attribute("contactEmail", EMAIL_ADDRESS))
        .andExpect(request().sessionAttribute("JOURNEY", Matchers.nullValue()));
    verify(mockCookieManager).removeCookie(any());
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/application-submitted"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
    verifyZeroInteractions(mockCookieManager);
  }

  @Test
  public void show_givenInvalidState_ShouldRedirectBackToStart() throws Exception {
    Journey journey = new Journey();
    mockMvc
        .perform(get("/application-submitted").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
    verifyZeroInteractions(mockCookieManager);
  }
}
