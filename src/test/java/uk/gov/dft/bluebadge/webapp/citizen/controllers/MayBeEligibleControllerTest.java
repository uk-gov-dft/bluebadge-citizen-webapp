package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
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
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class MayBeEligibleControllerTest {

  private MockMvc mockMvc;
  private MayBeEligibleController controller;

  @Mock private RouteMaster mockRouteMaster;
  @Mock private ReferenceDataService referenceDataService;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new MayBeEligibleController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourney();
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
  }

  @Test
  @SneakyThrows
  public void show_ShouldDisplayEligibleTemplate() {
    LocalAuthorityRefData testLARefData = new LocalAuthorityRefData();
    testLARefData.setShortCode("Bob");
    journey.setLocalAuthority(testLARefData);

    mockMvc
        .perform(get("/may-be-eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("may-be-eligible"))
        .andExpect(model().attribute("formRequest", Matchers.nullValue()))
        .andExpect(model().attribute("localAuthority", testLARefData));
  }

  @Test
  @SneakyThrows
  public void whenIssuingFormNotSet_thenRedirectBackToStart() {
    mockMvc
        .perform(get("/may-be-eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  @SneakyThrows
  public void startApplication_ShouldRedirectToNextPage() {

    when(mockRouteMaster.redirectToOnSuccess(StepDefinition.MAY_BE_ELIGIBLE))
        .thenReturn("redirect:/theNextPage");

    mockMvc
        .perform(get("/may-be-eligible/start"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/theNextPage"));
  }
}
