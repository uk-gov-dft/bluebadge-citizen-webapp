package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;

public class WalkingTimeControllerTest {

  private MockMvc mockMvc;
  private WalkingTimeController controller;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new WalkingTimeController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    ApplicantForm applicantForm =
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.toString()).build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_TIME, EligibilityCodeField.WALKD);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    // We are not testing the route master. So for convenience just forward to an error view so
    // can test the error messages
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("/someValidationError");
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/walking-time"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getWalkingTimeForm()))
        .andExpect(model().attributeExists("walkingTimeOptions"));
  }

  @Test
  public void show_walkingTimeOptionsShouldBeSet() throws Exception {
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("walkingTimeOptions"))
        .andReturn();
  }

  @Test
  public void show_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues() throws Exception {
    WalkingTimeForm form =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.LESSMIN).build();

    journey.setFormForStep(form);
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("walking/walking-time"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/walking-time").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {
    WalkingTimeForm form =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.LESSMIN).build();

    when(mockRouteMaster.redirectToOnSuccess(form)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/walking-time")
                .param("walkingTime", "LESSMIN")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    WalkingTimeForm form = WalkingTimeForm.builder().build();

    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any())).thenCallRealMethod();

    mockMvc
        .perform(
            post("/walking-time")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/walking-time#error"))
        .andExpect(flash().attribute("formRequest", form));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldHaveNotEmptyErrorMessage() throws Exception {
    mockMvc
        .perform(
            post("/walking-time")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("/someValidationError"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "walkingTime", "NotNull"));
  }
}
