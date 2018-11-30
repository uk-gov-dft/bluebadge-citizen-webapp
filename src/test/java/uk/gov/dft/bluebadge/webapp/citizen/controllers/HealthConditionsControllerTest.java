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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class HealthConditionsControllerTest {

  private MockMvc mockMvc;

  private Journey journey;

  @Before
  public void setup() {
    HealthConditionsController controller = new HealthConditionsController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new JourneyBuilder().toStep(StepDefinition.HEALTH_CONDITIONS).build();
  }

  @Test
  public void show_ShouldDisplayHealthConditionsTemplate() throws Exception {
    mockMvc
        .perform(get("/health-conditions").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("health-conditions"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getHealthConditionsForm()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/health-conditions"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/health-conditions")
                .param("descriptionOfConditions", "test test")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_PROVE_IDENTITY));
  }

  @Test
  public void submit_whenMissingDescriptionOfConditions_ThenShouldHaveValidationError()
      throws Exception {
    mockMvc
        .perform(post("/health-conditions").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_HEALTH_CONDITIONS + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "descriptionOfConditions", "NotNull"));
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
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_HEALTH_CONDITIONS + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "descriptionOfConditions", "Size"));
  }
}
