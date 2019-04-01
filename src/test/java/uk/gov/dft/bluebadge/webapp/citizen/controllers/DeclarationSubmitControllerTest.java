package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationSubmitForm;

class DeclarationSubmitControllerTest {

  private MockMvc mockMvc;

  Journey journey;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    DeclarationSubmitController controller =
        new DeclarationSubmitController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new JourneyBuilder().build();
  }

  @Test
  void show_ShouldDisplayDeclarationTemplate() throws Exception {

    DeclarationSubmitForm formRequest = DeclarationSubmitForm.builder().build();

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/declaration"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  void show_givenInvalidState_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  void submit_ShouldDisplayApplicationSubmittedTemplate_WhenDeclarationIsAgreed() throws Exception {

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
  }

  @Test
  void
      submit_ShouldDisplayApplicationSubmittedTemplateAndNotCreateApplication_WhenDeclarationIsAgreedAndPaymentsAreEnabled()
          throws Exception {

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr(
                    "JOURNEY",
                    JourneyFixture.getDefaultJourneyToStep(null, EligibilityCodeField.PIP, true)))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
  }

  @Test
  void submit_shouldSendFormDataWithinApplication_WhenDeclarationIsAgreed() throws Exception {

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
  }

  @Test
  void submit_ShouldThrowValidationError_WhenDeclarationIsNotAgreed() throws Exception {
    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "false"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_DECLARATIONS + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "agreed", "AssertTrue"));
  }
}
