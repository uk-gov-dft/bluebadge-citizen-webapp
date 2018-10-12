package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

public class TreatmentAddControllerTest extends ControllerTestFixture<TreatmentAddController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new TreatmentAddController(mockRouteMaster));
    journey.setTreatmentListForm(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
  }

  @Override
  String getTemplateName() {
    return "treatment-add";
  }

  @Override
  String getUrl() {
    return "/add-treatment";
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    super.show_ShouldDisplayTemplate();
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    super.show_shouldRedirect_whenJourneyNotSetup();
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post(getUrl())
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
            post(getUrl())
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
