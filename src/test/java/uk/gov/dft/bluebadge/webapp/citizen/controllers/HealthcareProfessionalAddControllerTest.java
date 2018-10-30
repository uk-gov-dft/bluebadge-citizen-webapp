package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

public class HealthcareProfessionalAddControllerTest
    extends ControllerTestFixture<HealthcareProfessionalAddController> {

  @Before
  public void setup() {
    super.setup(new HealthcareProfessionalAddController(new RouteMaster()));
    journey.setFormForStep(
        HealthcareProfessionalListForm.builder()
            .healthcareProfessionals(new ArrayList<>())
            .build());
  }

  @Override
  protected String getTemplateName() {
    return "healthcare-professional-add";
  }

  @Override
  protected String getUrl() {
    return "/add-healthcare-professional";
  }

  @Override
  protected StepDefinition getStep() {
    return StepDefinition.HEALTHCARE_PROFESSIONALS_ADD;
  }

  @Override
  protected EligibilityCodeField getEligibilityType() {
    return EligibilityCodeField.WALKD;
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
                .param("healthcareProfessionalName", "Dr No")
                .param("healthcareProfessionalLocation", "Spooky Island")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-healthcare-professionals"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {

    HealthcareProfessionalAddForm form = new HealthcareProfessionalAddForm();
    form.setId("1234");

    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-healthcare-professional#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode("healthcareProfessionalName", "NotBlank"))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "healthcareProfessionalLocation", "NotBlank"));
  }
}
