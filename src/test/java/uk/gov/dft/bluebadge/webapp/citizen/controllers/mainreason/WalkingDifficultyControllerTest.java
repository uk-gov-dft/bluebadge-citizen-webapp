package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType.YOURSELF;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.DANGEROUS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.HELP;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PAIN;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PLAN;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;

public class WalkingDifficultyControllerTest {

  private MockMvc mockMvc;
  private WalkingDifficultyController controller;

  @Mock private RouteMaster mockRouteMaster;
  @Mock private LocalAuthorityRefData mockLocalAuthority;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new WalkingDifficultyController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new Journey();
    journey.setApplicantForm(ApplicantForm.builder().applicantType(YOURSELF.name()).build());
    when(mockLocalAuthority.getNation()).thenReturn(Nation.ENG);
    journey.setLocalAuthority(mockLocalAuthority);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayWalkingDifficultyTemplate_WithEngNation() throws Exception {

    WalkingDifficultyForm formRequest = WalkingDifficultyForm.builder().build();

    when(mockLocalAuthority.getNation()).thenReturn(Nation.ENG);

    RadioOptionsGroup options =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("walkingDifficultyPage.content.title", journey)
            .addOptionApplicantAware(HELP, "options.walkingDifficultyPage.help", journey)
            .addOptionApplicantAware(PAIN, "options.walkingDifficultyPage.pain", journey)
            .addOptionApplicantAndNationAware(DANGEROUS, "options.walkingDifficultyPage.dangerous", journey)
            .addOptionApplicantAware(NONE, "options.walkingDifficultyPage.none", journey)
            .build();

    mockMvc
        .perform(get("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/walking-difficulty"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("formOptions", options));
  }

  @Test
  public void show_ShouldDisplayWalkingDifficultyTemplate_WithScoNation() throws Exception {

    WalkingDifficultyForm formRequest = WalkingDifficultyForm.builder().build();

    when(mockLocalAuthority.getNation()).thenReturn(Nation.SCO);

    RadioOptionsGroup options =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("walkingDifficultyPage.content.title", journey)
            .addOptionApplicantAware(HELP, "options.walkingDifficultyPage.help", journey)
            .addOptionApplicantAware(PLAN, "options.walkingDifficultyPage.plan", journey)
            .addOptionApplicantAware(PAIN, "options.walkingDifficultyPage.pain", journey)
            .addOptionApplicantAndNationAware(DANGEROUS, "options.walkingDifficultyPage.dangerous", journey)
            .addOptionApplicantAware(NONE, "options.walkingDifficultyPage.none", journey)
            .build();

    mockMvc
        .perform(get("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/walking-difficulty"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("formOptions", options));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/walking-difficulty"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(WalkingDifficultyForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/walking-difficulty")
                .param("walkingDifficulty", "NONE")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingWalkingDifficultyAnswer_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }
}
