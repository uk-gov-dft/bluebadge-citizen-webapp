package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.ENG;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class OrganisationNotEligibleControllerTest {

  private MockMvc mockMvc;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @BeforeEach
  void beforeEachTest(TestInfo testInfo) {
    log.info(String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  void afterEachTest(TestInfo testInfo) {
    log.info(String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }

  @BeforeAll
  void setup() {
    MockitoAnnotations.initMocks(this);
    OrganisationNotEligibleController controller = new OrganisationNotEligibleController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        new JourneyBuilder()
            .forOrganisation()
            .orgDoesCare(Boolean.FALSE)
            .toStep(StepDefinition.ORGANISATION_NOT_ELIGIBLE)
            .build();
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  @SneakyThrows
  @DisplayName("Should display organisation may be eligible template")
  public void show_Template() {
    mockMvc
        .perform(get("/organisation-not-eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("organisation/organisation-not-eligible"))
        .andExpect(model().attribute("formRequest", Matchers.nullValue()))
        .andExpect(model().attribute("localAuthority", JourneyFixture.getLocalAuthorityRefData(ENG)));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should redirect back to start if issuing authority is absent")
  public void whenIssuingFormNotSet_thenRedirectBackToStart() {
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/organisation-not-eligible"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }
}
