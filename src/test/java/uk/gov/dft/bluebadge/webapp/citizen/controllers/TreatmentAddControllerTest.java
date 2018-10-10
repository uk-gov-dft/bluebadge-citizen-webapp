package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;

import java.util.ArrayList;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

public class TreatmentAddControllerTest {
  private MockMvc mockMvc;
  private TreatmentAddController controller;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new TreatmentAddController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    ApplicantForm applicantForm =
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.toString()).build();

    journey = new Journey();
    journey.setApplicantForm(applicantForm);
    journey.setTreatmentListForm(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any())).thenCallRealMethod();
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/add-treatment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("treatment-add"))
        .andExpect(model().attributeExists("formRequest"));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/add-treatment").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/add-treatment")
                .param("treatmentDescription", "A description")
                .param("treatmentWhen", "when")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-treatments"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    TreatmentAddForm form = new TreatmentAddForm();
    form.setId("1234");

    mockMvc
        .perform(
            post("/add-treatment")
                .param("id", "1234")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-treatment"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("treatmentDescription", "NotBlank"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("treatmentWhen", "NotBlank"));
  }
}
