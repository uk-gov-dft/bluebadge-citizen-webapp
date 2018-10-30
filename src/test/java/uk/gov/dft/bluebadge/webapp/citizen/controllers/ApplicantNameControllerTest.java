package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.FormObjectToParamMapper;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;

public class ApplicantNameControllerTest extends ControllerTestFixture<ApplicantNameController> {

  @Before
  public void setup() {
    super.setup(new ApplicantNameController(new RouteMaster()));
  }

  @Test
  public void showApplicantName_ShouldDisplayTemplate() throws Exception {
    super.show_ShouldDisplayTemplate();
  }

  @Test
  public void shouldRedirectWhenJourneyNotSetup() throws Exception {
    super.show_shouldRedirect_whenJourneyNotSetup();
  }

  @Test
  public void ShowApplicantName_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues()
      throws Exception {
    ApplicantNameForm sessionApplicantNameForm =
        ApplicantNameForm.builder().fullName("John").hasBirthName(true).birthName("Doe").build();

    journey.setFormForStep(sessionApplicantNameForm);
    mockMvc
        .perform(get("/name").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant-name"))
        .andExpect(model().attribute("formRequest", sessionApplicantNameForm));
  }

  @Test
  public void submitApplicantName_ShouldMoveDirectApplicantToNextStepInJourney() throws Exception {
    ApplicantNameForm applicantNameForm =
        ApplicantNameForm.builder().fullName("John").hasBirthName(false).birthName("John").build();

    mockMvc
        .perform(
            post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_DOB));
  }

  @Test
  public void submitApplicationName_ShouldDisplayValidation_WhenMandatoryFieldsAreNotSet()
      throws Exception {
    ApplicantNameForm applicantNameForm = ApplicantNameForm.builder().build();

    mockMvc
        .perform(
            post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("formRequest", applicantNameForm))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("fullName", "NotBlank"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("hasBirthName", "NotNull"));
  }

  @Test
  public void submitApplicationName_ShouldDisplayValidation_BirthNameIsNotSet() throws Exception {
    ApplicantNameForm applicantNameForm =
        ApplicantNameForm.builder().fullName("John").hasBirthName(true).birthName("").build();

    mockMvc
        .perform(
            post("/name")
                .params(FormObjectToParamMapper.convert(applicantNameForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("formRequest", applicantNameForm))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode("birthName", "field.birthName.NotBlank"));
  }

  @Override
  protected String getTemplateName() {
    return "applicant-name";
  }

  @Override
  protected String getUrl() {
    return "/name";
  }

  @Override
  protected StepDefinition getStep() {
    return StepDefinition.NAME;
  }

  @Override
  protected EligibilityCodeField getEligibilityType() {
    return EligibilityCodeField.PIP;
  }
}
