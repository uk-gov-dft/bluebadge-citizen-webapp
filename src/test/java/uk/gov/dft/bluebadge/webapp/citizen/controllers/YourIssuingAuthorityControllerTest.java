package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class YourIssuingAuthorityControllerTest {

  private MockMvc mockMvc;
  @Mock ReferenceDataService mockReferenceDataService;

  Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    YourIssuingAuthorityController controller =
        new YourIssuingAuthorityController(new RouteMaster(), mockReferenceDataService);
    journey = new JourneyBuilder().toStep(StepDefinition.YOUR_ISSUING_AUTHORITY).build();
    when(mockReferenceDataService.lookupLocalAuthorityFromCouncilCode(anyString()))
        .thenReturn(JourneyFixture.getLocalAuthorityRefData(Nation.ENG));
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_whenFirstvisit() throws Exception {
    Journey journey = new JourneyBuilder().toStep(StepDefinition.CHOOSE_COUNCIL).build();
    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode("WARCC");
    journey.setLocalAuthority(localAuthorityRefData);
    mockMvc
        .perform(get(Mappings.URL_YOUR_ISSUING_AUTHORITY).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("issuing-authority"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getYourIssuingAuthorityForm()));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(post(Mappings.URL_YOUR_ISSUING_AUTHORITY).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_EXISTING_BADGE));
  }
}
