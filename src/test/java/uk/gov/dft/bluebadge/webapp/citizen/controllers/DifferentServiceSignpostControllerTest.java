package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;

public class DifferentServiceSignpostControllerTest {

  private static final String DEFAULT_DIFFERENT_SERVICE_SIGNPOST_URL =
      "https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here";
  private MockMvc mockMvc;

  Journey journey;

  @Mock private RouteMaster routeMasterMock;
  @Mock private Journey journeyMock;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    journey = new JourneyBuilder().toStep(StepDefinition.DIFFERENT_SERVICE_SIGNPOST).build();

    DifferentServiceSignpostController controller =
        new DifferentServiceSignpostController(
            routeMasterMock, DEFAULT_DIFFERENT_SERVICE_SIGNPOST_URL);

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode("WARCC");
    LocalAuthorityRefData.LocalAuthorityMetaData localAuthorityMetaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    localAuthorityMetaData.setDifferentServiceSignpostUrl(DEFAULT_DIFFERENT_SERVICE_SIGNPOST_URL);
    when(journeyMock.getLocalAuthority()).thenReturn(localAuthorityRefData);
  }

  @Test
  @SneakyThrows
  public void show_ShouldDisplayDifferentServiceSignPostPage_WhenYouAreInAValidState() {
    when(routeMasterMock.isValidState(StepDefinition.DIFFERENT_SERVICE_SIGNPOST, journeyMock))
        .thenReturn(true);

    mockMvc
        .perform(get("/different-service-signpost").sessionAttr("JOURNEY", journeyMock))
        .andExpect(status().isOk())
        .andExpect(view().name("different-service-signpost"));
  }

  @Test
  @SneakyThrows
  public void show_ShouldRedirectBackToCompletePreviousPage_WhenYouAreInAnInValidState() {
    when(routeMasterMock.isValidState(StepDefinition.DIFFERENT_SERVICE_SIGNPOST, journeyMock))
        .thenReturn(false);
    when(routeMasterMock.backToCompletedPrevious(journey)).thenReturn("redirect:/backToCompletePrevious");

    mockMvc
        .perform(get("/different-service-signpost").sessionAttr("JOURNEY", journeyMock))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToCompletePrevious"));
  }

  @Test
  @SneakyThrows
  public void redirectToChooseCouncil_shouldRedirectToChooseCouncil() {
    journey.setFormForStep(FindYourCouncilForm.builder().postcode("M41FS").build());
    mockMvc
        .perform(
            get(Mappings.URL_DIFFERENT_SERVICE_SIGNPOST_CHOOSE_COUNCIL)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/choose-council"));
    FindYourCouncilForm emptyForm = FindYourCouncilForm.builder().postcode("").build();
    FindYourCouncilForm actualForm = journey.getFormForStep(StepDefinition.FIND_COUNCIL);
    assertThat(actualForm).isEqualTo(emptyForm);
  }

  @Test
  @SneakyThrows
  public void redirectToThirdParty_shouldRedirectToThirdPartyWebsite() {
    journey.setFormForStep(FindYourCouncilForm.builder().postcode("M41FS").build());
    HttpSession session =
        mockMvc
            .perform(
                get(Mappings.URL_DIFFERENT_SERVICE_SIGNPOST_CONTINUE)
                    .sessionAttr("JOURNEY", journey))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("https://bluebadge.direct.gov.uk/bluebadge/why-are-you-here"))
            .andReturn()
            .getRequest()
            .getSession();
    assertThat(session.getAttribute("JOURNEY")).isNull();
  }
}
