package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.YesNoType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;

public class MentalDisorderControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    MentalDisorderController controller = new MentalDisorderController(new RouteMaster());

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.AFCS_MENTAL_DISORDER, EligibilityCodeField.AFRFCS, Nation.WLS);

    CompensationSchemeForm form = journey.getFormForStep(StepDefinition.AFCS_COMPENSATION_SCHEME);
    form.setHasReceivedCompensation(Boolean.FALSE);
  }

  @Test
  public void show_ShouldDisplayDisabilityTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.afcs.mentalDisorderPage.title").withYesNoOptions(YesNoType.IAM);

    mockMvc
        .perform(get("/permanent-mental-disorder").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("afcs/mental-disorder"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getMentalDisorderForm()))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {

    mockMvc
        .perform(post("/permanent-mental-disorder").param("hasMentalDisorder", ""))
        .andExpect(redirectedUrl(Mappings.URL_AFCS_MENTAL_DISORDER + RouteMaster.ERROR_SUFFIX));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/permanent-mental-disorder"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
