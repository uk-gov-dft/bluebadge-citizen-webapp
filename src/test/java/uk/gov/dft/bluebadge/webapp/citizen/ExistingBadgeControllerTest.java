package uk.gov.dft.bluebadge.webapp.citizen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType.YOURSELF;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ExistingBadgeController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

public class ExistingBadgeControllerTest {

  private MockMvc mockMvc;
  private ExistingBadgeController controller;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;
  private static final String NINO = "NS123456A";
  private static final String INVALID_NINO = "NS123456";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ExistingBadgeController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = new Journey();
    journey.setApplicantForm(ApplicantForm.builder().applicantType(YOURSELF.name()).build());
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    ExistingBadgeForm form = ExistingBadgeForm.builder().build();

    mockMvc
        .perform(get("/existing-badge").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("existing-badge"))
        .andExpect(model().attribute("formRequest", form));
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

    ExistingBadgeForm form = ExistingBadgeForm.builder().build();

    mockMvc
            .perform(get("/existing-badge-bypass").sessionAttr("JOURNEY", journey))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/testSuccess"));
  }
}
