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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;

public class EligibleControllerTest {

  private MockMvc mockMvc;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    EligibleController controller = new EligibleController(mockRouteMaster);
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
    YourIssuingAuthorityForm yourIssuingAuthorityForm =
        YourIssuingAuthorityForm.builder().localAuthorityShortCode("bob").build();
    journey.setYourIssuingAuthorityForm(yourIssuingAuthorityForm);
    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    journey.setLocalAuthority(localAuthorityRefData);

    mockMvc
        .perform(get("/eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("eligible"))
        .andExpect(model().attribute("formRequest", Matchers.nullValue()))
        .andExpect(model().attribute("localAuthority", localAuthorityRefData));
  }

  @Test
  @SneakyThrows
  public void whenIssuingFormNotSet_thenRedirectBackToStart() {
    mockMvc
        .perform(
            get("/eligible")
                .sessionAttr(
                    "JOURNEY",
                    JourneyFixture.getDefaultJourneyToStep(StepDefinition.APPLICANT_TYPE)))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  @SneakyThrows
  public void startApplication_ShouldRedirectToNextPage() {

    when(mockRouteMaster.redirectToOnSuccess(StepDefinition.ELIGIBLE))
        .thenReturn("redirect:/theNextPage");

    mockMvc
        .perform(get("/eligible/start"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/theNextPage"));
  }
}
