package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.ENG;

public class WhatWalkingDifficultiesControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    WhatWalkingDifficultiesController controller =
        new WhatWalkingDifficultiesController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WHAT_WALKING_DIFFICULTIES, WALKD, ENG);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");

    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("/someValidationError");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/what-walking-difficult"))
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
    LocalAuthorityRefData.LocalAuthorityMetaData metaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    metaData.setNation(Nation.SCO);
    testLA.setLocalAuthorityMetaData(Optional.of(metaData));

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
            .toStep(StepDefinition.WHAT_WALKING_DIFFICULTIES)
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
        .andExpect(view().name("walking/what-walking-difficult"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {
    List<WalkingDifficultyTypeCodeField> whatList =
        ImmutableList.of(
            WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.SOMELSE);
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(whatList)
            .somethingElseDescription("test test")
            .build();

    when(mockRouteMaster.redirectToOnSuccess(form)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/what-makes-walking-difficult")
                .param("whatWalkingDifficulties", "PAIN, SOMELSE")
                .param("somethingElseDescription", "test test")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    WhatMakesWalkingDifficultForm form = WhatMakesWalkingDifficultForm.builder().build();

    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any())).thenCallRealMethod();

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
        .andExpect(status().isOk())
        .andExpect(view().name("/someValidationError"))
        .andExpect(
            model()
                .attributeHasFieldErrorCode("formRequest", "whatWalkingDifficulties", "NotEmpty"));
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
        .andExpect(status().isOk())
        .andExpect(view().name("/someValidationError"))
        .andExpect(
            model()
                .attributeHasFieldErrorCode("formRequest", "somethingElseDescription", "NotBlank"));
  }
}
