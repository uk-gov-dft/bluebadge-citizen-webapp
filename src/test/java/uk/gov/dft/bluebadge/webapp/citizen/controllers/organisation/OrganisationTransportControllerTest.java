package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
class OrganisationTransportControllerTest {

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
    OrganisationTransportController controller =
        new OrganisationTransportController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        new JourneyBuilder()
            .forOrganisation()
            .toStep(StepDefinition.ORGANISATION_TRANSPORT)
            .build();
  }

  @Test
  @SneakyThrows
  @DisplayName("Should display organisation transport template")
  void show_Template() {
    mockMvc
        .perform(get("/organisation-transport").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("organisation/organisation-transport"))
        .andExpect(model().attribute("formRequest", Matchers.notNullValue()))
        .andExpect(model().attribute("options", Matchers.notNullValue()));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should redirect back to start if session is absent")
  void show_redirectBackToStart() {
    mockMvc
        .perform(get("/organisation-transport"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should redirect to success when either option is selected")
  void submit_redirectToSuccess() {

    mockMvc
        .perform(
            post("/organisation-transport")
                .param("doesTransport", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ORGANISATION_MAY_BE_ELIGIBLE));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should redirect to success when either option is selected")
  void submit_redirectToSuccess_false() {

    mockMvc
        .perform(
            post("/organisation-transport")
                .param("doesTransport", "false")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ORGANISATION_NOT_ELIGIBLE));
  }

  @Test
  @SneakyThrows
  @DisplayName("Should return validation error when none of the options selected")
  void submit_validationError() {
    mockMvc
        .perform(post("/organisation-transport").sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ORGANISATION_TRANSPORT + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "doesTransport", "NotNull"));
  }
}
