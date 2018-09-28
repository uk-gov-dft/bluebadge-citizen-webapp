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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

public class NinoControllerTest {

  private MockMvc mockMvc;
  private NinoController controller;
  @Mock private RouteMaster mockRouteMaster;

  private Journey journey;
  private String nino = "NS123456A";
  private String invalidNino = "NS123456";

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

    DateOfBirthForm dob = DateOfBirthForm.builder()
            .day("12").month("04").year("1988").build();
    journey.setDateOfBirthForm(dob);

    mockMvc
            .perform(get("/nino").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("nino"))
            .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_ShouldByPassNinoTemplate_WhenApplicantIsYoung() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(NinoForm.class)))
            .thenReturn("redirect:/testSuccess");

    NinoForm form = NinoForm.builder().build();
    DateOfBirthForm dob = DateOfBirthForm.builder()
            .day("12").month("04").year("2002").build();

    journey.setDateOfBirthForm(dob);

    mockMvc
            .perform(get("/nino").sessionAttr("JOURNEY", journey))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_GivenAValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(NinoForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/nino").param("nino", nino))
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
        .perform(post("/nino").sessionAttr("JOURNEY", new Journey()).param("nino", invalidNino))
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
}
