package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class DeclarationSubmitControllerTest {

  private MockMvc mockMvc;

  @Mock ApplicationManagementService appService;
  @Mock private RouteMaster mockRouteMaster;
  @Mock Journey mockJourney;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    DeclarationSubmitController controller = new DeclarationSubmitController(appService, mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showDeclaration_ShouldDisplayDeclarationTemplate() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);

    DeclarationForm formRequest = DeclarationForm.builder().build();

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration").sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/declaration"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void showDeclaration_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void showDeclaration_givenInvalidState_ShouldRedirectBackToStart() throws Exception {

    when(mockJourney.isValidState(any())).thenReturn(false);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submitDeclaration_ShouldDisplayApplicationSubmittedTemplate_WhenDeclarationIsAgreed()
      throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(DeclarationForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    verify(appService, times(1)).create(any());
  }

  @Test
  public void submitDeclaration_shouldSendFormDataWithinApplication_WhenDeclarationIsAgreed()
      throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(DeclarationForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
    verify(appService, times(1)).create(captor.capture());

    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getEligibility()).isNotNull();
    assertThat(captor.getValue().getEligibility().getDescriptionOfConditions())
        .isEqualTo("test description - Able to walk to: London - How long: 10 minutes");
  }

  @Test
  public void submitDeclaration_ShouldThrowValidationError_WhenDeclarationIsNotAgreed()
      throws Exception {
    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "false"))
        .andExpect(status().isOk())
        .andExpect(view().name("apply-for-a-blue-badge/declaration"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "agreed", "AssertTrue"));
  }
}
