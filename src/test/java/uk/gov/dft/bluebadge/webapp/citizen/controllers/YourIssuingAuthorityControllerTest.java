package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.SneakyThrows;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class YourIssuingAuthorityControllerTest {

  private MockMvc mockMvc;
  @Mock ReferenceDataService mockReferenceDataService;

  Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    YourIssuingAuthorityController controller =
        new YourIssuingAuthorityController(
            RouteMasterFixture.routeMaster(), mockReferenceDataService);
    journey = new JourneyBuilder().toStep(StepDefinition.YOUR_ISSUING_AUTHORITY).build();
    when(mockReferenceDataService.lookupLocalAuthorityFromCouncilCode(anyString()))
        .thenReturn(JourneyFixture.getLocalAuthorityRefData(Nation.ENG, false));
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  @SneakyThrows
  public void show_whenFirstvisit() {
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

  @Test
  @SneakyThrows
  public void redirectToChooseCouncil_shouldRedirectToChooseCouncil() {
    journey.setFormForStep(FindYourCouncilForm.builder().postcode("M41FS").build());
    mockMvc
        .perform(
            get(Mappings.URL_YOUR_ISSUING_AUTHORITY_CHOOSE_COUNCIL).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/choose-council"));
    FindYourCouncilForm emptyForm = FindYourCouncilForm.builder().postcode("").build();
    FindYourCouncilForm actualForm = journey.getFormForStep(StepDefinition.FIND_COUNCIL);
    assertThat(actualForm).isEqualTo(emptyForm);
  }
}
