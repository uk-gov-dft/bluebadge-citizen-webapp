package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.ENG;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class WhatMakesWalkingDifficultControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  private static final String ERROR_URL =
      Mappings.URL_WHAT_MAKES_WALKING_DIFFICULT + RouteMaster.ERROR_SUFFIX;

  @Before
  public void setup() {
    WhatMakesWalkingDifficultController controller =
        new WhatMakesWalkingDifficultController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WHAT_MAKES_WALKING_DIFFICULT, WALKD, ENG, false);
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/what-makes-walking-difficult"))
        .andExpect(
            model().attribute("formRequest", JourneyFixture.getWhatMakesWalkingDifficultForm()))
        .andExpect(model().attributeExists("walkingDifficulties"));
  }

  @Test
  public void show_givenNationEngland_walkingDifficultiesShouldBeNationSpecific() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("walkingDifficulties"))
            .andReturn();

    RadioOptionsGroup options =
        (RadioOptionsGroup)
            Objects.requireNonNull(mvcResult.getModelAndView())
                .getModel()
                .get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("DANGER", "oth.ENG.whatMakesWalkingDifficult.select.option.dangerous"))
        .doesNotContain(tuple("STRUGGLE", "oth.whatMakesWalkingDifficult.select.option.struggle"));
  }

  @Test
  public void show_givenNationScotland_walkingDifficultiesShouldBeNationSpecific()
      throws Exception {
    LocalAuthorityRefData testLA = new LocalAuthorityRefData();
    testLA.setShortCode("ELOTH");
    LocalAuthorityRefData.LocalAuthorityMetaData metaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    metaData.setNation(Nation.SCO);
    testLA.setLocalAuthorityMetaData(metaData);

    journey.setLocalAuthority(testLA);

    MvcResult mvcResult =
        mockMvc
            .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("walkingDifficulties"))
            .andReturn();

    RadioOptionsGroup options =
        (RadioOptionsGroup)
            Objects.requireNonNull(mvcResult.getModelAndView())
                .getModel()
                .get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("DANGER", "oth.SCO.whatMakesWalkingDifficult.select.option.dangerous"))
        .contains(tuple("STRUGGLE", "oth.whatMakesWalkingDifficult.select.option.struggle"));
  }

  @Test
  public void show_givenNationWales_walkingDifficultiesShouldBeNationSpecific() throws Exception {
    Journey wales =
        new JourneyBuilder()
            .inWales()
            .toStep(StepDefinition.WHAT_MAKES_WALKING_DIFFICULT)
            .withEligibility(WALKD)
            .build();

    MvcResult mvcResult =
        mockMvc
            .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", wales))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("walkingDifficulties"))
            .andReturn();

    RadioOptionsGroup options =
        (RadioOptionsGroup)
            Objects.requireNonNull(mvcResult.getModelAndView())
                .getModel()
                .get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("STRUGGLE", "oth.whatMakesWalkingDifficult.select.option.struggle"));
  }

  @Test
  public void show_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues() throws Exception {
    List<WalkingDifficultyTypeCodeField> whatList =
        ImmutableList.of(
            WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.SOMELSE);
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(whatList)
            .somethingElseDescription("test test")
            .build();

    journey.setFormForStep(form);
    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/what-makes-walking-difficult"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .param("whatWalkingDifficulties", "PAIN, SOMELSE")
                .param("somethingElseDescription", "test test")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_MOBILITY_AID_LIST));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_whenBreathlessnessIsSelected()
      throws Exception {

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .param("whatWalkingDifficulties", "PAIN, BREATH")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_BREATHLESS));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    WhatMakesWalkingDifficultForm form = WhatMakesWalkingDifficultForm.builder().build();

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/what-makes-walking-difficult#error"))
        .andExpect(flash().attribute("formRequest", form));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldHaveNotEmptyErrorMessage() throws Exception {

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "whatWalkingDifficulties", "NotEmpty"));
  }

  @Test
  public void
      submit_whenSomethingElseSelectedAndNoDescription_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .param("whatWalkingDifficulties", "PAIN, SOMELSE")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "somethingElseDescription", "NotBlank"));
  }
}
