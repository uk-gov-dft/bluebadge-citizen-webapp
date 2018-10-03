package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.apache.commons.lang3.StringUtils;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
    journey = new Journey();
    journey.setApplicantForm(
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.name()).build());
  }

  @Test
  public void show_ShouldDisplayWhereCanYouWalkTemplate() throws Exception {
    WhereCanYouWalkForm formRequest = WhereCanYouWalkForm.builder().build();

    mockMvc
        .perform(get("/where-can-you-walk").sessionAttr("JOURNEY", journey))
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
        .andExpect(status().isOk())
        .andExpect(view().name("where-can-you-walk"))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "destinationToHome", "NotBlank"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "timeToDestination", "NotBlank"))
        .andExpect(model().errorCount(2));
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
        .andExpect(status().isOk())
        .andExpect(view().name("where-can-you-walk"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "destinationToHome", "Size"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "timeToDestination", "Size"))
        .andExpect(model().errorCount(2));
  }
}
