package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.DANGEROUS;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.HELP;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PAIN;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm.WalkingDifficulty.PLAN;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;

public class WalkingDifficultyControllerTest {

  private MockMvc mockMvc;

  @Before
  public void setup() {
    WalkingDifficultyController controller = new WalkingDifficultyController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDisplayWalkingDifficultyTemplate_WithEngNation() throws Exception {
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_DIFFICULTY, EligibilityCodeField.WALKD, Nation.ENG, false);

    RadioOptionsGroup options =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("walkingDifficultyPage.content.title", journey)
            .addOptionApplicantAware(HELP, "options.walkingDifficultyPage.help", journey)
            .addOptionApplicantAware(PAIN, "options.walkingDifficultyPage.pain", journey)
            .addOptionApplicantAndNationAware(
                DANGEROUS, "options.walkingDifficultyPage.dangerous", journey)
            .addOptionApplicantAware(NONE, "options.walkingDifficultyPage.none", journey)
            .build();

    mockMvc
        .perform(get("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/walking-difficulty"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getWalkingDifficultyForm()))
        .andExpect(model().attribute("formOptions", options));
  }

  @Test
  public void show_ShouldDisplayWalkingDifficultyTemplate_WithScoNation() throws Exception {

    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_DIFFICULTY, EligibilityCodeField.WALKD, Nation.SCO, false);

    RadioOptionsGroup options =
        new RadioOptionsGroup.Builder()
            .titleMessageKeyApplicantAware("walkingDifficultyPage.content.title", journey)
            .addOptionApplicantAware(HELP, "options.walkingDifficultyPage.help", journey)
            .addOptionApplicantAware(PLAN, "options.walkingDifficultyPage.plan", journey)
            .addOptionApplicantAware(PAIN, "options.walkingDifficultyPage.pain", journey)
            .addOptionApplicantAndNationAware(
                DANGEROUS, "options.walkingDifficultyPage.dangerous", journey)
            .addOptionApplicantAware(NONE, "options.walkingDifficultyPage.none", journey)
            .build();

    mockMvc
        .perform(get("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mainreason/walking-difficulty"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getWalkingDifficultyForm()))
        .andExpect(model().attribute("formOptions", options));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/walking-difficulty"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_DIFFICULTY, EligibilityCodeField.WALKD, Nation.ENG, false);

    mockMvc
        .perform(
            post("/walking-difficulty")
                .param("walkingDifficulty", "NONE")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_NOT_ELIGIBLE));
  }

  @Test
  public void submit_whenMissingWalkingDifficultyAnswer_ThenShouldHaveValidationError()
      throws Exception {
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_DIFFICULTY, EligibilityCodeField.WALKD, Nation.ENG, false);
    mockMvc
        .perform(post("/walking-difficulty").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_WALKING_DIFFICULTY + RouteMaster.ERROR_SUFFIX));
  }
}
