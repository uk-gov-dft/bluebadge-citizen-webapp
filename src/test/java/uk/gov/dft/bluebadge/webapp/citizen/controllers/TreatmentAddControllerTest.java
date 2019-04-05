package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.TreatmentWhenType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

public class TreatmentAddControllerTest extends ControllerTestFixture<TreatmentAddController> {

  @Before
  public void setup() {
    super.setup(new TreatmentAddController(new RouteMaster()));
    journey.setFormForStep(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
  }

  @Override
  protected String getTemplateName() {
    return "treatment-add";
  }

  @Override
  protected String getUrl() {
    return "/add-treatment";
  }

  @Override
  protected StepDefinition getStep() {
    return StepDefinition.TREATMENT_ADD;
  }

  @Override
  protected EligibilityCodeField getEligibilityType() {
    return EligibilityCodeField.WALKD;
  }

  private String DESCRIPTION = "A description";

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    super.show_ShouldDisplayTemplate();
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    super.show_shouldRedirect_whenJourneyNotSetup();
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withValidPastTreatmentDetails()
      throws Exception {

    mockMvc
        .perform(
            post(getUrl())
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "PAST")
                .param("treatmentPastWhen", "10th May 2017")
                .param("treatmentWhen", "10th May 2017")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-treatments"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withValidOngoingTreatmentDetails()
      throws Exception {

    mockMvc
        .perform(
            post(getUrl())
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "ONGOING")
                .param("treatmentOngoingFrequency", "Forthnightly")
                .param("treatmentWhen", "Ongoing - Forthnightly")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-treatments"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withValidFutureTreatmentDetails()
      throws Exception {

    mockMvc
        .perform(
            post(getUrl())
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "FUTURE")
                .param("treatmentFutureWhen", "21/21/2121")
                .param("treatmentFutureImprove", "Yes")
                .param("treatmentWhen", "21/21/2121 - - Expected to improve? Yes")
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
        .andExpect(redirectedUrl("/add-treatment#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("treatmentDescription", "NotBlank"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("treatmentWhenType", "NotNull"));
  }

  @Test
  public void
      submit_whenDescriptionIsSet_andNoDateOptionChosen_andFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    TreatmentAddForm form = new TreatmentAddForm();
    form.setTreatmentDescription(DESCRIPTION);
    form.setId("1234");
    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .param("treatmentDescription", DESCRIPTION)
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-treatment#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("treatmentWhenType", "NotNull"));
  }

  @Test
  public void
      submit_whenDescriptionIsSet_andPastDateOptionChosen_andNoDateSet_andFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    TreatmentAddForm form = new TreatmentAddForm();
    form.setId("1234");
    form.setTreatmentDescription(DESCRIPTION);
    form.setTreatmentWhenType(TreatmentWhenType.PAST);
    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "PAST")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-treatment#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "treatmentPastWhen", "NotNull.treatment.fields.treatmentPastWhen"));
  }

  @Test
  public void
      submit_whenDescriptionIsSet_andOngoingDateOptionChosen_andNoFrequencySet_andFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    TreatmentAddForm form = new TreatmentAddForm();
    form.setId("1234");
    form.setTreatmentDescription(DESCRIPTION);
    form.setTreatmentWhenType(TreatmentWhenType.ONGOING);
    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "ONGOING")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-treatment#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "treatmentOngoingFrequency", "NotNull.treatment.fields.treatmentOngoingFrequency"));
  }

  @Test
  public void
      submit_whenDescriptionIsSet_andFutureDateOptionChosen_andNoDateSet_andFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    TreatmentAddForm form = new TreatmentAddForm();
    form.setId("1234");
    form.setTreatmentDescription(DESCRIPTION);
    form.setTreatmentWhenType(TreatmentWhenType.FUTURE);
    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .param("treatmentDescription", DESCRIPTION)
                .param("treatmentWhenType", "FUTURE")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-treatment#error"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "treatmentFutureWhen", "NotNull.treatment.fields.treatmentFutureWhen"))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "treatmentFutureImprove", "NotNull.treatment.fields.treatmentFutureImprove"));
  }
}
