package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class DifferentServiceSignpostControllerTest {

  public static final String DIFFERENT_SERVICE_SIGNPOST_URL =
      "https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here";
  private MockMvc mockMvc;
  private LocalAuthorityRefData localAuthorityRefData;

  @Mock private RouteMaster routeMasterMock;
  @Mock private Journey journeyMock;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    DifferentServiceSignpostController controller =
        new DifferentServiceSignpostController(routeMasterMock);

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode("WARCC");
    LocalAuthorityRefData.LocalAuthorityMetaData localAuthorityMetaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    localAuthorityMetaData.setDifferentServiceSignpostUrl(DIFFERENT_SERVICE_SIGNPOST_URL);
    when(journeyMock.getLocalAuthority()).thenReturn(localAuthorityRefData);
  }

  @Test
  public void show_ShouldDisplayDifferentServiceSignPostPage_WhenYouAreInAValidState()
      throws Exception {
    when(routeMasterMock.isValidState(StepDefinition.DIFFERENT_SERVICE_SIGNPOST, journeyMock))
        .thenReturn(true);

    mockMvc
        .perform(get("/different-service-signpost").sessionAttr("JOURNEY", journeyMock))
        .andExpect(status().isOk())
        .andExpect(view().name("different-service-signpost"))
        .andExpect(model().attribute("localAuthority", localAuthorityRefData))
        .andExpect(
            model().attribute("differentServiceSignpostUrl", DIFFERENT_SERVICE_SIGNPOST_URL));
  }

  @Test
  public void show_ShouldRedirectBackToCompletePreviousPage_WhenYouAreInAnInValidState()
      throws Exception {
    when(routeMasterMock.isValidState(StepDefinition.DIFFERENT_SERVICE_SIGNPOST, journeyMock))
        .thenReturn(false);
    when(routeMasterMock.backToCompletedPrevious()).thenReturn("redirect:/backToCompletePrevious");

    mockMvc
        .perform(get("/different-service-signpost").sessionAttr("JOURNEY", journeyMock))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToCompletePrevious"));
  }
}
