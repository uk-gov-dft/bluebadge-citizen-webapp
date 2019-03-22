package uk.gov.dft.bluebadge.webapp.citizen.controllers.blind;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.PermissionForm;

public class PermissionControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    PermissionController controller = new PermissionController(RouteMasterFixture.routeMaster());

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.REGISTERED, EligibilityCodeField.BLIND, Nation.ENG, false);
  }

  @Test
  public void show_ShouldDisplayPermissionTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.permissionPage.title").withYesNoOptions();
    options.setHintKey("oth.permissionPage.hint");
    PermissionForm emptyForm = PermissionForm.builder().build();

    mockMvc
        .perform(get("/permission").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/permission"))
        .andExpect(model().attribute("formRequest", emptyForm))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void show_ShouldDisplayPermissionTemplateFromSession_WhenSessionExists() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.permissionPage.title").withYesNoOptions();
    options.setHintKey("oth.permissionPage.hint");

    PermissionForm form = PermissionForm.builder().hasPermission(true).build();
    journey.setFormForStep(form);

    mockMvc
        .perform(get("/permission").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/permission"))
        .andExpect(model().attribute("formRequest", form))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/permission"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {
    mockMvc
        .perform(post("/permission"))
        .andExpect(redirectedUrl(Mappings.URL_PERMISSION + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void submit_ShouldReturnToRegisteredCouncil_WhenYesIsSelected() throws Exception {
    mockMvc
        .perform(post("/permission").sessionAttr("JOURNEY", journey).param("hasPermission", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_REGISTERED_COUNCIL));
  }

  @Test
  public void submit_ShouldReturnToDeclarations_WhenNoIsSelected() throws Exception {
    mockMvc
        .perform(
            post("/permission").sessionAttr("JOURNEY", journey).param("hasPermission", "false"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PROVE_IDENTITY));
  }
}
