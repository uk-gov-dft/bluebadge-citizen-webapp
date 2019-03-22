package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE;

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
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
class OrganisationMayBeEligibleControllerTest {

  private MockMvc mockMvc;
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
    OrganisationMayBeEligibleController controller =
        new OrganisationMayBeEligibleController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        new JourneyBuilder()
            .inEngland()
            .forOrganisation()
            .toStep(ORGANISATION_MAY_BE_ELIGIBLE)
            .build();
  }

  @Test
  @SneakyThrows
  @DisplayName("Should display organisation may be eligible template")
  void show_Template() {
    mockMvc
        .perform(get("/organisation-may-be-eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("organisation/organisation-may-be-eligible"))
        .andExpect(model().attribute("formRequest", Matchers.nullValue()));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should redirect back to start if issuing authority is absent")
  void whenIssuingFormNotSet_thenRedirectBackToStart() {

    mockMvc
        .perform(get("/organisation-may-be-eligible"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
