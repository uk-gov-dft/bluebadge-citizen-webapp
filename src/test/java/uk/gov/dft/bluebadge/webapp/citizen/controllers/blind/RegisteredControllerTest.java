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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredForm;

public class RegisteredControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    RegisteredController controller = new RegisteredController(RouteMasterFixture.routeMaster());

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.CONTACT_DETAILS, EligibilityCodeField.BLIND, Nation.ENG, false);
  }

  @Test
  public void show_ShouldDisplayRegisteredTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.registeredPage.title").withYesNoOptions();
    RegisteredForm emptyRegisteredForm = RegisteredForm.builder().build();

    mockMvc
        .perform(get("/registered").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/registered"))
        .andExpect(model().attribute("formRequest", emptyRegisteredForm))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void show_ShouldDisplayRegisteredTemplateFromSession_WhenSessionExists() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.registeredPage.title").withYesNoOptions();
    RegisteredForm registeredForm = RegisteredForm.builder().hasRegistered(true).build();
    journey.setFormForStep(registeredForm);

    mockMvc
        .perform(get("/registered").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("blind/registered"))
        .andExpect(model().attribute("formRequest", registeredForm))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/registered"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {

    mockMvc
        .perform(post("/registered"))
        .andExpect(redirectedUrl(Mappings.URL_REGISTERED + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void submit_ShouldReturnToPermission_WhenYesIsSelected() throws Exception {

    mockMvc
        .perform(post("/registered").sessionAttr("JOURNEY", journey).param("hasRegistered", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PERMISSION));
  }

  @Test
  public void submit_ShouldReturnToDeclarations_WhenNoIsSelected() throws Exception {

    mockMvc
        .perform(
            post("/registered").sessionAttr("JOURNEY", journey).param("hasRegistered", "false"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PROVE_IDENTITY));
  }
}
