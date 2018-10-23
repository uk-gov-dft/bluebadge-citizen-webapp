package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertTrue;
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

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;

public class WhereCanYouWalkControllerTest {

  private static final String DESTINATION_TO_HOME_MIN = "L";
  private static final String DESTINATION_TO_HOME = "London";
  private static final String DESTINATION_TO_HOME_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String DESTINATION_TO_HOME_OVER_MAX = DESTINATION_TO_HOME_MAX + "a";

  private static final String TIME_TO_DESTINATION_MIN = "1";
  private static final String TIME_TO_DESTINATION = "10 minutes";;
  private static final String TIME_TO_DESTINATION_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String TIME_TO_DESTINATION_OVER_MAX = TIME_TO_DESTINATION_MAX + "a";

  private MockMvc mockMvc;
  private WhereCanYouWalkController controller;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new WhereCanYouWalkController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.WALKING_TIME, EligibilityCodeField.WALKD);

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayWhereCanYouWalkTemplateWithEmptyForm() throws Exception {
    WhereCanYouWalkForm formRequest = WhereCanYouWalkForm.builder().build();

    mockMvc
        .perform(get("/where-can-you-walk").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("where-can-you-walk"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_ShouldDisplayWhereCanYouWalkTemplateWithNonEmptyForm() throws Exception {
    WhereCanYouWalkForm formRequest =
        WhereCanYouWalkForm.builder()
            .destinationToHome("destination")
            .timeToDestination("10 minutes")
            .build();

    journey.setWhereCanYouWalkForm(formRequest);

    mockMvc
        .perform(
            get("/where-can-you-walk")
                //.flashAttr("formRequest", formRequest)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("where-can-you-walk"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/where-can-you-walk"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenEmptyValues_thenShouldDisplayNotBlankValidationErrors() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(WhereCanYouWalkForm.class), any(Journey.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", "")
                .param("timeToDestination", "")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));

    ArgumentCaptor<BindingResult> captor = ArgumentCaptor.forClass(BindingResult.class);
    verify(mockRouteMaster, times(1))
        .redirectToOnBindingError(any(), any(), captor.capture(), any());
    BindingResult bindingResult = (BindingResult) captor.getValue();

    assertThat(bindingResult.getErrorCount()).isEqualTo(2);
    assertTrue(containsError(bindingResult.getFieldErrors(), "destinationToHome", "", "NotBlank"));
    assertTrue(containsError(bindingResult.getFieldErrors(), "timeToDestination", "", "NotBlank"));
  }

  @Test
  public void submit_givenMinimumValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(WhereCanYouWalkForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_MIN)
                .param("timeToDestination", TIME_TO_DESTINATION_MIN)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(WhereCanYouWalkForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME)
                .param("timeToDestination", TIME_TO_DESTINATION)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidFormWithMaxValues_thenShouldDisplayRedirectToSuccess()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(WhereCanYouWalkForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_MAX)
                .param("timeToDestination", TIME_TO_DESTINATION_MAX)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenValidFormWithOverMaxValues_thenShouldDisplaySizeValidationErrors()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(WhereCanYouWalkForm.class), any(Journey.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_OVER_MAX)
                .param("timeToDestination", TIME_TO_DESTINATION_OVER_MAX)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));

    ArgumentCaptor<BindingResult> captor = ArgumentCaptor.forClass(BindingResult.class);
    verify(mockRouteMaster, times(1))
        .redirectToOnBindingError(any(), any(), captor.capture(), any());
    BindingResult bindingResult = (BindingResult) captor.getValue();

    assertThat(bindingResult.getErrorCount()).isEqualTo(2);
    assertTrue(
        containsError(
            bindingResult.getFieldErrors(),
            "destinationToHome",
            DESTINATION_TO_HOME_OVER_MAX,
            "Size"));
    assertTrue(
        containsError(
            bindingResult.getFieldErrors(),
            "timeToDestination",
            TIME_TO_DESTINATION_OVER_MAX,
            "Size"));
  }

  private boolean containsError(
      List<FieldError> fieldErrors, String field, String rejectedValue, String errorCode) {
    for (FieldError fieldError : fieldErrors) {
      if (isEqual(fieldError, field, rejectedValue, errorCode)) {
        return true;
      }
      ;
    }
    return false;
  }

  private boolean isEqual(
      FieldError fieldError, String field, String rejectedValue, String errorCode) {
    return (fieldError.getField().equalsIgnoreCase(field)
        && ((String) fieldError.getRejectedValue()).equalsIgnoreCase(rejectedValue)
        && fieldError.getCode().equalsIgnoreCase(errorCode));
  }
}
