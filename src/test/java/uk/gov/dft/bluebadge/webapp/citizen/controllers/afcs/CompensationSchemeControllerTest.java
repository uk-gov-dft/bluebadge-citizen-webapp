package uk.gov.dft.bluebadge.webapp.citizen.controllers.afcs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.AFCS_COMPENSATION_SCHEME;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType.YOURSELF;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;

public class CompensationSchemeControllerTest {

  private MockMvc mockMvc;
  private CompensationSchemeController controller;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new CompensationSchemeController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = JourneyFixture.getDefaultJourneyToStep(AFCS_COMPENSATION_SCHEME, EligibilityCodeField.AFRFCS);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayCompensationScheme_WithRadioOptions() throws Exception {
    RadioOptionsGroup options =
        new RadioOptionsGroup("oth.afcs.compensationSchemePage.title").withYesNoOptions();


    mockMvc
        .perform(get("/lump-sum").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("afcs/compensation-scheme"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getCompensationSchemeForm()))
        .andExpect(model().attribute("radioOptions", options));
  }

  @Test
  public void submit_ShouldDisplayErrors_WhenNoOptionsAreSelected() throws Exception {

    mockMvc
        .perform(post("/lump-sum").param("hasReceivedCompensation", ""))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/lump-sum"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }
}
