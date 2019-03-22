package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeCount;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class NinoControllerTest {

  private MockMvc mockMvc;

  private Journey journey;
  private static final String NINO = "NS123456A";
  private static final String INVALID_NINO = "NS12345";

  private static final String SUCCESS_URL = Mappings.URL_ENTER_ADDRESS;
  private static final String ERROR_URL = Mappings.URL_NINO + RouteMaster.ERROR_SUFFIX;

  @Before
  public void setup() {
    NinoController controller = new NinoController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.NINO);
  }

  @Test
  public void show_ShouldDisplayNinoTemplate() throws Exception {

    mockMvc
        .perform(get("/nino").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("nino"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getNinoForm()));
  }

  @Test
  public void submit_GivenAValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    mockMvc
        .perform(post("/nino").param("nino", NINO).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_whenNoNinoIsProvided_ThenShouldDisplayValidationError() throws Exception {

    mockMvc
        .perform(post("/nino").sessionAttr("JOURNEY", new Journey()).param("nino", ""))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("nino", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(1));
  }

  @Test
  public void submit_whenInvalidNinoIsProvided_ThenShouldDisplayValidationError() throws Exception {

    mockMvc
        .perform(post("/nino").sessionAttr("JOURNEY", new Journey()).param("nino", INVALID_NINO))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("nino", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(1));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/nino"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {

    mockMvc
        .perform(get("/nino-bypass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }
}
