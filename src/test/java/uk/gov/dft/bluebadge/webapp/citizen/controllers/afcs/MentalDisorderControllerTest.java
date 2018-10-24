package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;

public class MentalDisorderControllerTest {

  private MockMvc mockMvc;
  private MentalDisorderController controller;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    controller = new MentalDisorderController(mockRouteMaster);

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.AFCS_MENTAL_DISORDER, EligibilityCodeField.AFRFCS, Nation.WLS);

    CompensationSchemeForm form = journey.getFormForStep(StepDefinition.AFCS_COMPENSATION_SCHEME);
    form.setHasReceivedCompensation(Boolean.FALSE);

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayDisabilityTemplate_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.afcs.mentalDisorderPage.title").withYesNoOptions();

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
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/permanent-mental-disorder"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }
}
