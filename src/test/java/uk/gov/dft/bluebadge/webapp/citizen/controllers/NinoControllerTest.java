package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType.YOURSELF;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

public class NinoControllerTest {

  private MockMvc mockMvc;
  private NinoController controller;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;
  private static final String NINO = "NS123456A";
  private static final String INVALID_NINO = "NS123456";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new NinoController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey = new Journey();
    journey.setApplicantForm(ApplicantForm.builder().applicantType(YOURSELF.name()).build());
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any()))
        .thenReturn("redirect:/someValidationError");
  }

  @Test
  public void show_ShouldDisplayNinoTemplate() throws Exception {

    NinoForm form = NinoForm.builder().build();

    mockMvc
        .perform(get("/nino").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("nino"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void submit_GivenAValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(NinoForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/nino").param("nino", NINO))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenNoNinoIsProvided_ThenShouldDisplayValidationError() throws Exception {

    mockMvc
        .perform(post("/nino").sessionAttr("JOURNEY", new Journey()).param("nino", ""))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void submit_whenInvalidNinoIsProvided_ThenShouldDisplayValidationError() throws Exception {

    mockMvc
        .perform(post("/nino").sessionAttr("JOURNEY", new Journey()).param("nino", INVALID_NINO))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/nino"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void onByPassLink_ShouldRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(NinoForm.class)))
        .thenReturn("redirect:/testSuccess");

    NinoForm form = NinoForm.builder().build();

    mockMvc
        .perform(get("/nino-bypass").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }
}
