package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.AFRFCS;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.AFCS_DISABILITY;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.YesNoType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.DisabilityForm;

import java.util.Optional;

public class DisabilityControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    DisabilityController controller = new DisabilityController(new RouteMaster());

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(AFCS_DISABILITY, AFRFCS);
  }

  @Test
  public void show_ShouldDisplayDisabilityTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.afcs.disabilityPage.title").withYesNoOptions(Optional.of(YesNoType.IAM));

    DisabilityForm form = DisabilityForm.builder().hasDisability(Boolean.TRUE).build();

    mockMvc
        .perform(get("/permanent-and-substantial-disability").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("afcs/disability"))
        .andExpect(model().attribute("formRequest", form))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {

    mockMvc
        .perform(post("/permanent-and-substantial-disability").param("hasDisability", ""))
        .andExpect(redirectedUrl(Mappings.URL_AFCS_DISABILITY + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {
    mockMvc
        .perform(get("/permanent-and-substantial-disability"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
