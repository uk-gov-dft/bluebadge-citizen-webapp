package uk.gov.dft.bluebadge.webapp.citizen.controllers.PIP;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType.YOURSELF;

import org.hamcrest.Matchers;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PIP.PipDlaQuestionForm;

public class PipDlaQuestionControllerTest {
  private MockMvc mockMvc;
  private PipDlaQuestionController controller;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new PipDlaQuestionController(mockRouteMaster);
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
  public void show_ShouldDisplayDlaTemplate() throws Exception {

    PipDlaQuestionForm formRequest = PipDlaQuestionForm.builder().build();

    mockMvc
        .perform(get("/dla-in-the-past").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("pip/received-dla"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("formOptions", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/dla-in-the-past"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(PipDlaQuestionForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/dla-in-the-past")
                .param("receivedDlaOption", "HAS_RECEIVED_DLA")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingDlaAnswer_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post("/dla-in-the-past").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/someValidationError"));
  }
}