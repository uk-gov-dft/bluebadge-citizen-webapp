package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MEDICATION_LIST;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;

public class MedicationListControllerTest extends ControllerTestFixture<MedicationListController> {

  private static final String SUCCESS_URL = Mappings.URL_TASK_LIST;

  @Before
  public void setup() {
    super.setup(new MedicationListController(RouteMasterFixture.routeMaster()));
    journey.setFormForStep(MedicationListForm.builder().medications(new ArrayList<>()).build());
  }

  @Override
  protected String getTemplateName() {
    return "walking/medication-list";
  }

  @Override
  protected String getUrl() {
    return "/list-medication";
  }

  @Override
  protected StepDefinition getStep() {
    return MEDICATION_LIST;
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
                .param("hasMedication", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withMedications() throws Exception {

    List<MedicationAddForm> medications = new ArrayList<>();
    MedicationAddForm medication = new MedicationAddForm();
    medication.setDosage("Dosage");
    medication.setFrequency("Frequency");
    medication.setName("Name");
    medication.setPrescribed("yes");
    medications.add(medication);

    MedicationListForm journeyForm = journey.getFormForStep(MEDICATION_LIST);
    journeyForm.setHasMedication("yes");
    journeyForm.setMedications(medications);

    mockMvc
        .perform(
            post(getUrl())
                .param("hasMedication", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    // Get again - will be different object in journey after controller round trip
    journeyForm = journey.getFormForStep(MEDICATION_LIST);
    assertEquals("yes", journeyForm.getHasMedication());
    assertEquals(1, journeyForm.getMedications().size());
  }

  @Test
  public void submit_setHasMedicationsToNoIfEmpty() throws Exception {

    MedicationListForm journeyForm = journey.getFormForStep(MEDICATION_LIST);
    journeyForm.setHasMedication("yes");
    journeyForm.setMedications(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasMedication", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
    // Then has reset to no. Reget form - will be different object in journey.
    journeyForm = journey.getFormForStep(MEDICATION_LIST);
    assertEquals("no", journeyForm.getHasMedication());
  }

  @Test
  public void submit_bindingError() throws Exception {

    mockMvc
        .perform(
            post(getUrl())
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "hasMedication", "NotNull"));
  }

  @Test
  public void remove() throws Exception {
    mockMvc
        .perform(
            get(getUrl() + "/remove").sessionAttr("JOURNEY", new Journey()).param("uuid", "1234"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(getUrl()));
  }
}
