package uk.gov.dft.bluebadge.webapp.citizen.controllers.walking;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;

public class MedicationListControllerTest extends ControllerTestFixture<MedicationListController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new MedicationListController(mockRouteMaster));
    journey.setMedicationListForm(
        MedicationListForm.builder().medications(new ArrayList<>()).build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
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
    return StepDefinition.MEDICATION_LIST;
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
    when(mockRouteMaster.redirectToOnSuccess(any(MedicationListForm.class)))
        .thenReturn("redirect:/testSuccess");
    mockMvc
        .perform(
            post(getUrl())
                .param("hasMedication", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withMedications() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MedicationListForm.class)))
        .thenReturn("redirect:/testSuccess");

    List<MedicationAddForm> medications = new ArrayList<>();
    MedicationAddForm medication = new MedicationAddForm();
    medication.setDosage("Dosage");
    medication.setFrequency("Frequency");
    medication.setName("Name");
    medication.setPrescribed("yes");
    medications.add(medication);

    journey.getMedicationListForm().setHasMedication("yes");
    journey.getMedicationListForm().setMedications(medications);

    mockMvc
        .perform(
            post(getUrl())
                .param("hasMedication", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    assertEquals("yes", journey.getMedicationListForm().getHasMedication());
    assertEquals(1, journey.getMedicationListForm().getMedications().size());
  }

  @Test
  public void submit_setHasMedicationsToNoIfEmpty() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MedicationListForm.class)))
        .thenReturn("redirect:/testSuccess");

    journey.getMedicationListForm().setHasMedication("yes");
    journey.getMedicationListForm().setMedications(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasMedication", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
    // Then has reset to no.
    assertEquals("no", journey.getMedicationListForm().getHasMedication());
  }

  @Test
  public void submit_bindingError() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MedicationListForm.class)))
        .thenReturn("redirect:/testSuccess");

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
