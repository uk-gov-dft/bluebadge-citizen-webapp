package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.CAST;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.OTHER;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.ENG;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MedicalEquipmentForm;

public class MedicalEquipmentControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  private static final String ERROR_URL = Mappings.URL_MEDICAL_EQUIPMENT + RouteMaster.ERROR_SUFFIX;

  @Before
  public void setup() {
    MedicalEquipmentController controller = new MedicalEquipmentController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(StepDefinition.MEDICAL_EQUIPMENT, CHILDBULK, ENG);
  }

  @Test
  public void show_shouldDisplayTemplate() throws Exception {

    mockMvc
        .perform(get("/medical-equipment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("medical-equipment"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getMedicalEquipmentForm()))
        .andExpect(model().attributeExists("equipment"));
  }

  @Test
  public void show_shouldDisplayTemplate_withPrePopulatedFormSessionValues() throws Exception {
    List<BulkyMedicalEquipmentTypeCodeField> equipment = ImmutableList.of(CAST, OTHER);

    MedicalEquipmentForm form = JourneyFixture.getMedicalEquipmentForm();
    form.setEquipment(equipment);

    journey.setFormForStep(form);
    mockMvc
        .perform(get("/medical-equipment").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("medical-equipment"))
        .andExpect(model().attribute("formRequest", form));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/medical-equipment").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/medical-equipment")
                .param("equipment", "SYRINGE, OTHER")
                .param("otherDescription", "other description")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_HEALTHCARE_PROFESSIONALS_LIST));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {

    MedicalEquipmentForm form = MedicalEquipmentForm.builder().build();

    mockMvc
        .perform(
            post("/medical-equipment")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/medical-equipment#error"))
        .andExpect(flash().attribute("formRequest", form));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldHaveNotEmptyErrorMessage() throws Exception {

    mockMvc
        .perform(
            post("/medical-equipment")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "equipment", "NotEmpty"));
  }

  @Test
  public void
      submit_whenOtherSelectedAndNoDescription_thenShouldRedirectToShowWithValidationErrors()
          throws Exception {

    mockMvc
        .perform(
            post("/medical-equipment")
                .param("equipment", "OTHER")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "otherDescription", "NotBlank"));
  }
}
