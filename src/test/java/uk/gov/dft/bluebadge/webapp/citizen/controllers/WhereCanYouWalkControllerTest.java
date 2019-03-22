package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;

public class WhereCanYouWalkControllerTest {

  private static final String DESTINATION_TO_HOME_MIN = "L";
  private static final String DESTINATION_TO_HOME = "London";
  private static final String DESTINATION_TO_HOME_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String DESTINATION_TO_HOME_OVER_MAX = DESTINATION_TO_HOME_MAX + "a";

  private static final String TIME_TO_DESTINATION_MIN = "1";
  private static final String TIME_TO_DESTINATION = "10 minutes";
  private static final String TIME_TO_DESTINATION_MAX = StringUtils.leftPad("a", 100, 'b');
  private static final String TIME_TO_DESTINATION_OVER_MAX = TIME_TO_DESTINATION_MAX + "a";

  private static final String SUCCESS_URL = Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS;
  private static final String ERROR_URL =
      Mappings.URL_WHERE_CAN_YOU_WALK + RouteMaster.ERROR_SUFFIX;
  private MockMvc mockMvc;

  private Journey journey;

  @Before
  public void setup() {
    WhereCanYouWalkController controller =
        new WhereCanYouWalkController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_TIME, EligibilityCodeField.WALKD);
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

    journey.setFormForStep(formRequest);

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

    mockMvc
        .perform(get("/where-can-you-walk"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenEmptyValues_thenShouldDisplayNotBlankValidationErrors() throws Exception {

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", "")
                .param("timeToDestination", "")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "destinationToHome", "NotBlank"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "timeToDestination", "NotBlank"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  public void submit_givenMinimumValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_MIN)
                .param("timeToDestination", TIME_TO_DESTINATION_MIN)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME)
                .param("timeToDestination", TIME_TO_DESTINATION)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidFormWithMaxValues_thenShouldDisplayRedirectToSuccess()
      throws Exception {

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_MAX)
                .param("timeToDestination", TIME_TO_DESTINATION_MAX)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenValidFormWithOverMaxValues_thenShouldDisplaySizeValidationErrors()
      throws Exception {

    mockMvc
        .perform(
            post("/where-can-you-walk")
                .param("destinationToHome", DESTINATION_TO_HOME_OVER_MAX)
                .param("timeToDestination", TIME_TO_DESTINATION_OVER_MAX)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "destinationToHome", "Size"))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "timeToDestination", "Size"))
        .andExpect(redirectedUrl(ERROR_URL));
  }
}
