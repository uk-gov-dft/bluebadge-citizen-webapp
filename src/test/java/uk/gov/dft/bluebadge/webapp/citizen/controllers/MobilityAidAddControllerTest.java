package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;

public class MobilityAidAddControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    MobilityAidAddController controller = new MobilityAidAddController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.MOBILITY_AID_LIST, EligibilityCodeField.WALKD);
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/add-mobility-aid").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mobility-aid-add"))
        .andExpect(model().attributeExists("formRequest"));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/add-mobility-aid").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/add-mobility-aid")
                .param("aidType", "WALKING_AID")
                .param("howProvidedCodeField", "PRESCRIBE")
                .param("usage", "usage")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-mobility-aids"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {

    MobilityAidAddForm form = new MobilityAidAddForm();
    form.setId("1234");
    mockMvc
        .perform(
            post("/add-mobility-aid")
                .param("id", "1234")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-mobility-aid#error"))
        .andExpect(flash().attribute("formRequest", form));
  }
}
