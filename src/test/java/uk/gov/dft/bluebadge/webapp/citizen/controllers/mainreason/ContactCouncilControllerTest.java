package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

public class ContactCouncilControllerTest {
  private MockMvc mockMvc;
  private Journey journey;
  @Mock RedirectVersionCookieManager mockCookieManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ContactCouncilController controller =
        new ContactCouncilController(RouteMasterFixture.routeMaster(), mockCookieManager);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.CONTACT_COUNCIL, EligibilityCodeField.TERMILL);
  }

  @Test
  public void show_ShouldDisplayContactCouncilTemplate() throws Exception {

    mockMvc
        .perform(get("/contact-council").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/contact-council"));
    verify(mockCookieManager).removeCookie(any());
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/contact-council"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
    verifyZeroInteractions(mockCookieManager);
  }
}
