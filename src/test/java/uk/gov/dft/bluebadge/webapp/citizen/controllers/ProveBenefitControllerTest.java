package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.FormObjectToParamMapper;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveBenefitForm;

public class ProveBenefitControllerTest extends ControllerTestFixture<ProveBenefitController> {

  @Before
  public void setup() {
    super.setup(new ProveBenefitController(RouteMasterFixture.routeMaster()));
  }

  @Test
  public void showProveBenefit_ShouldDisplayTemplate() throws Exception {
    super.show_ShouldDisplayTemplate();
  }

  @Test
  public void shouldRedirectWhenJourneyNotSetup() throws Exception {
    super.show_shouldRedirect_whenJourneyNotSetup();
  }

  @Test
  public void ShowProveBenefit_ShouldDisplayTemplate_WithPrePopulatedFormSessionValues()
      throws Exception {
    ProveBenefitForm proveBenefitForm = ProveBenefitForm.builder().hasProof(true).build();

    journey.setFormForStep(proveBenefitForm);
    mockMvc
        .perform(get("/prove-benefit").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("prove-benefit"))
        .andExpect(model().attribute("formRequest", proveBenefitForm));
  }

  @Test
  public void submitProveBenefit_withYes_ShouldDirectApplicantToNextStepInJourney()
      throws Exception {

    ProveBenefitForm proveBenefitForm = ProveBenefitForm.builder().hasProof(true).build();

    mockMvc
        .perform(
            post("/prove-benefit")
                .params(FormObjectToParamMapper.convert(proveBenefitForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_UPLOAD_BENEFIT));
  }

  @Test
  public void submitProveBenefit_withNo_AndWithAValidDate_ShouldDirectApplicantToNextStepInJourney()
      throws Exception {

    LocalDate currentDate = LocalDate.now();
    CompoundDate date = new CompoundDate(currentDate);
    date.setYear(Integer.toString(currentDate.getYear() + 1));

    ProveBenefitForm proveBenefitForm =
        ProveBenefitForm.builder().hasProof(false).awardEndDate(date).build();

    mockMvc
        .perform(
            post("/prove-benefit")
                .param("hasProof", proveBenefitForm.getHasProof().toString())
                .param("awardEndDate.day", proveBenefitForm.getAwardEndDate().getDay())
                .param("awardEndDate.month", proveBenefitForm.getAwardEndDate().getMonth())
                .param("awardEndDate.year", proveBenefitForm.getAwardEndDate().getYear())
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_UPLOAD_BENEFIT));
  }

  @Test
  public void submitProveBenefit_ShouldDisplayValidation_WhenMandatoryFieldsAreNotSet()
      throws Exception {
    ProveBenefitForm proveBenefitForm = ProveBenefitForm.builder().build();

    mockMvc
        .perform(
            post("/prove-benefit")
                .params(FormObjectToParamMapper.convert(proveBenefitForm))
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("formRequest", proveBenefitForm))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("hasProof", "NotNull"));
  }

  @Test
  public void submitProveBenefit_ShouldDisplayValidation_hasProofIsToNo_AndWithoutADate()
      throws Exception {
    LocalDate currentDate = LocalDate.now();
    CompoundDate date = new CompoundDate(currentDate);
    date.setYear(Integer.toString(currentDate.getYear() - 1));

    ProveBenefitForm proveBenefitForm =
        ProveBenefitForm.builder().hasProof(false).awardEndDate(date).build();

    mockMvc
        .perform(
            post("/prove-benefit")
                .param("hasProof", proveBenefitForm.getHasProof().toString())
                .param("awardEndDate.day", proveBenefitForm.getAwardEndDate().getDay())
                .param("awardEndDate.month", proveBenefitForm.getAwardEndDate().getMonth())
                .param("awardEndDate.year", proveBenefitForm.getAwardEndDate().getYear())
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("formRequest", proveBenefitForm))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode("awardEndDate", "FutureCompoundDate"));
  }

  @Test
  public void submitProveBenefit_ShouldDisplayValidation_WhenDateIsNotInTheFuture()
      throws Exception {
    ProveBenefitForm proveBenefitForm =
        ProveBenefitForm.builder()
            .hasProof(false)
            .awardEndDate(new CompoundDate("", "", ""))
            .build();

    mockMvc
        .perform(
            post("/prove-benefit")
                .param("hasProof", proveBenefitForm.getHasProof().toString())
                .param("awardEndDate.day", proveBenefitForm.getAwardEndDate().getDay())
                .param("awardEndDate.month", proveBenefitForm.getAwardEndDate().getMonth())
                .param("awardEndDate.year", proveBenefitForm.getAwardEndDate().getYear())
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("formRequest", proveBenefitForm))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "awardEndDate", "ConditionalNotNull.awardEndDate"));
  }

  @Override
  protected String getTemplateName() {
    return "prove-benefit";
  }

  @Override
  protected String getUrl() {
    return "/prove-benefit";
  }

  @Override
  protected StepDefinition getStep() {
    return StepDefinition.PROVE_BENEFIT;
  }

  @Override
  protected EligibilityCodeField getEligibilityType() {
    return EligibilityCodeField.PIP;
  }
}
