package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MedicationAddControllerTest extends ControllerTestFixture<MedicationAddController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new MedicationAddController(mockRouteMaster));
    journey.setMedicationListForm(
        MedicationListForm.builder().medications(new ArrayList<>()).build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
  }

  @Override
  protected String getTemplateName() {
    return "walking/medication-add";
  }

  @Override
  protected String getUrl() {
    return "/add-medication";
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
                .param("name", "A name")
                .param("prescribed", "yes")
                .param("dosage", "Dosage")
                .param("frequency", "Frequency")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-medication"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {
    MedicationAddForm form = new MedicationAddForm();
    form.setId("1234");

    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-medication#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("name", "NotBlank"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("prescribed", "NotNull"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("dosage", "NotBlank"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("frequency", "NotBlank"));
  }
}
