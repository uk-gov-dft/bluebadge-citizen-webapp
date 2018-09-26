package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;

public class HigherRateMobilityControllerTest {

  private MockMvc mockMvc;
  private HigherRateMobilityController controller;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new HigherRateMobilityController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new Journey();
    journey.setApplicantForm(
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.name()).build());
  }

  @Test
  public void show_ShouldDisplayHigherRateMobilityTemplate() throws Exception {
    HigherRateMobilityForm formRequest = HigherRateMobilityForm.builder().build();

    mockMvc
        .perform(get("/higher-rate-mobility").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("higher-rate-mobility"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("options", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/higher-rate-mobility"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenYesOption_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HigherRateMobilityForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/higher-rate-mobility")
                .param("awardedHigherRateMobility", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenNoOption_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HigherRateMobilityForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/higher-rate-mobility")
                .param("awardedHigherRateMobility", "false")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingHigherRateMobilityAnswer_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/higher-rate-mobility").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("higher-rate-mobility"))
        .andExpect(
            model()
                .attributeHasFieldErrorCode("formRequest", "awardedHigherRateMobility", "NotNull"));
  }
}
