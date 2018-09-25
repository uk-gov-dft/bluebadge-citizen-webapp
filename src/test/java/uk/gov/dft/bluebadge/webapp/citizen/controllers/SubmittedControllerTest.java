package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class SubmittedControllerTest {

  private MockMvc mockMvc;
  private SubmittedController controller;

  @Mock private RouteMaster mockRouteMaster;
  @Mock Journey mockJourney;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new SubmittedController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showSubmitted_ShouldDisplaySubmittedTemplate_WhenThereIsAJourney() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);
    when(mockJourney.applicantContextContent("submittedPage.content.p1")).thenReturn("the message");

    mockMvc
        .perform(get("/application-submitted").sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/submitted"))
        .andExpect(model().attribute("mainMessage", "the message"))
        .andExpect(request().sessionAttribute("JOURNEY", Matchers.nullValue()));
  }

  @Test
  public void showSubmitted_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/application-submitted"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }
}
