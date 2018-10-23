package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ExistingBadgeControllerTest {

  private MockMvc mockMvc;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ExistingBadgeController controller = new ExistingBadgeController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.EXISTING_BADGE);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/existing-badge").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("existing-badge"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getExistingBadgeForm()));
  }

  @Test
  public void submit_GivenFormValueIs_No_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(ExistingBadgeForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/existing-badge").param("hasExistingBadge", "no"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_GivenFormValueIs_Yes_WithoutBadgeNumber_thenShouldDisplayError()
      throws Exception {
    mockMvc
        .perform(
            post("/existing-badge")
                .sessionAttr("JOURNEY", new Journey())
                .param("hasBadgeNumber", "yes"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void
      submit_GivenFormValueIs_Yes_WithBadgeNumberlessThan6Characters_thenShouldDisplayError()
          throws Exception {
    mockMvc
        .perform(
            post("/existing-badge")
                .sessionAttr("JOURNEY", new Journey())
                .param("hasBadgeNumber", "yes")
                .param("badgeNumber", "AB12"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void submit_GivenFormValueIs_Yes_WithoutBadgeNumberEntered_thenShouldRedirectToSuccess()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(ExistingBadgeForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/existing-badge").param("hasExistingBadge", "no").param("badgeNumber", "AB12CD"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(ExistingBadgeForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(get("/existing-badge-bypass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }
}
