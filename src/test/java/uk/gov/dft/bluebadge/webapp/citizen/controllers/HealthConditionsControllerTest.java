package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;

public class HealthConditionsControllerTest {

  private MockMvc mockMvc;
  private HealthConditionsController controller;

  @Mock Journey mockJourney;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new HealthConditionsController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldDisplayHealthConditionsTemplate() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);
    HealthConditionsForm formRequest = HealthConditionsForm.builder().build();

    mockMvc
        .perform(get("/health-conditions").sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("health-conditions"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/health-conditions"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(HealthConditionsForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/health-conditions")
                .param("descriptionOfConditions", "test test")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingDescriptionOfConditions_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/health-conditions").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("health-conditions"))
        .andExpect(
            model()
                .attributeHasFieldErrorCode("formRequest", "descriptionOfConditions", "NotNull"));
  }

  @Test
  public void submit_whenTooLongDescriptionOfConditions_ThenShouldHaveValidationError()
      throws Exception {
    String tooLong = StringUtils.leftPad("a", 9001, 'b');
    mockMvc
        .perform(
            post("/health-conditions")
                .param("descriptionOfConditions", tooLong)
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("health-conditions"))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "descriptionOfConditions", "Size"));
  }
}
