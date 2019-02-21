package uk.gov.dft.bluebadge.webapp.citizen.controllers.blind;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class RegisteredCouncilControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Mock private ReferenceDataService referenceDataServiceMock;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    RegisteredCouncilController controller =
        new RegisteredCouncilController(referenceDataServiceMock, new RouteMaster());

    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    LocalAuthorityRefData.LocalAuthorityMetaData localAuthorityMetaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    localAuthorityMetaData.setIssuingAuthorityShortCode("WARCC");
    localAuthorityRefData.setLocalAuthorityMetaData(localAuthorityMetaData);

    when(referenceDataServiceMock.lookupLocalAuthorityFromCouncilCode("WARCC"))
        .thenReturn(localAuthorityRefData);

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PERMISSION, EligibilityCodeField.BLIND, Nation.ENG, false);
  }

  @Test
  public void show_ShouldDisplayRegisteredCouncilTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.registeredCouncilPage.title").withYesNoOptions();
    RegisteredCouncilForm emptyForm = RegisteredCouncilForm.builder().build();

    mockMvc
        .perform(get("/registered-council").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/registered-council"))
        .andExpect(model().attribute("formRequest", emptyForm));
  }

  @Test
  public void show_ShouldDisplayRegisteredCouncilTemplateFromSession_WhenSessionExists()
      throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.registeredCouncilPage.title").withYesNoOptions();

    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    LocalAuthorityRefData.LocalAuthorityMetaData localAuthorityMetaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    localAuthorityMetaData.setIssuingAuthorityShortCode("WARCC");
    localAuthorityRefData.setLocalAuthorityMetaData(localAuthorityMetaData);

    RegisteredCouncilForm form =
        RegisteredCouncilForm.builder()
            .localAuthorityForRegisteredBlind(localAuthorityRefData)
            .build();
    journey.setFormForStep(form);

    mockMvc
        .perform(get("/registered-council").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/registered-council"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/registered-council"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {
    mockMvc
        .perform(post("/registered-council"))
        .andExpect(redirectedUrl(Mappings.URL_REGISTERED_COUNCIL + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void submit_ShouldReturnToDeclarations_WhenCouncilIsSelected() throws Exception {
    mockMvc
        .perform(
            post("/registered-council")
                .sessionAttr("JOURNEY", journey)
                .param("registeredCouncil", "WARCC"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PROVE_IDENTITY));
  }
}
