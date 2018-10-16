package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

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

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class WhatWalkingDifficultiesControllerTest {

  private MockMvc mockMvc;
  private WhatWalkingDifficultiesController controller;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new WhatWalkingDifficultiesController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    ApplicantForm applicantForm =
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.toString()).build();
    LocalAuthorityRefData testLA = new LocalAuthorityRefData();
    LocalAuthorityRefData.LocalAuthorityMetaData metaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    metaData.setNation(Nation.ENG);
    testLA.setLocalAuthorityMetaData(Optional.of(metaData));

    journey = new Journey();
    journey.setApplicantForm(applicantForm);
    journey.setLocalAuthority(testLA);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    // We are not testing the route master. So for convenience just forward to an error view so
    // can test the error messages
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("/someValidationError");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    WhatMakesWalkingDifficultForm form = WhatMakesWalkingDifficultForm.builder().build();

    mockMvc
        .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/what-walking-difficult"))
        .andExpect(model().attribute("formRequest", form))
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
        (RadioOptionsGroup) mvcResult.getModelAndView().getModel().get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("DANGER", "you.ENG.whatMakesWalkingDifficult.select.option.dangerous"))
        .doesNotContain(tuple("STRUGGLE", "you.whatMakesWalkingDifficult.select.option.struggle"));
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
        (RadioOptionsGroup) mvcResult.getModelAndView().getModel().get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("DANGER", "you.SCO.whatMakesWalkingDifficult.select.option.dangerous"))
        .contains(tuple("STRUGGLE", "you.whatMakesWalkingDifficult.select.option.struggle"));
  }

  @Test
  public void show_givenNationWales_walkingDifficultiesShouldBeNationSpecific() throws Exception {
    LocalAuthorityRefData testLA = new LocalAuthorityRefData();
    LocalAuthorityRefData.LocalAuthorityMetaData metaData =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    metaData.setNation(Nation.WLS);
    testLA.setLocalAuthorityMetaData(Optional.of(metaData));

    journey.setLocalAuthority(testLA);

    MvcResult mvcResult =
        mockMvc
            .perform(get("/what-makes-walking-difficult").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("walkingDifficulties"))
            .andReturn();

    RadioOptionsGroup options =
        (RadioOptionsGroup) mvcResult.getModelAndView().getModel().get("walkingDifficulties");
    assertThat(options.getOptions())
        .extracting("shortCode", "messageKey")
        .contains(tuple("STRUGGLE", "you.whatMakesWalkingDifficult.select.option.struggle"));
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

    journey.setWhatMakesWalkingDifficultForm(form);
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
    WhatMakesWalkingDifficultForm form = WhatMakesWalkingDifficultForm.builder().build();

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
    List<WalkingDifficultyTypeCodeField> whatList =
        ImmutableList.of(
            WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.SOMELSE);
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder().whatWalkingDifficulties(whatList).build();

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
