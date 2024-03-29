package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_LIST;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

public class TreatmentListControllerTest extends ControllerTestFixture<TreatmentListController> {

  private static final String SUCCESS_URL = Mappings.URL_TASK_LIST;

  @Before
  public void setup() {
    super.setup(new TreatmentListController(RouteMasterFixture.routeMaster()));
    journey.setFormForStep(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
  }

  @Override
  protected String getTemplateName() {
    return "treatment-list";
  }

  @Override
  protected String getUrl() {
    return "/list-treatments";
  }

  @Override
  protected StepDefinition getStep() {
    return TREATMENT_LIST;
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
                .param("hasTreatment", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withTreatments() throws Exception {

    TreatmentAddForm form = new TreatmentAddForm();
    form.setTreatmentDescription("A");
    form.setTreatmentWhen("F");

    TreatmentListForm journeyForm = journey.getFormForStep(TREATMENT_LIST);
    journeyForm.setHasTreatment("yes");
    journeyForm.setTreatments(Lists.newArrayList(form));

    mockMvc
        .perform(
            post(getUrl())
                .param("hasTreatment", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    // Get again - will be different instance in journey now
    journeyForm = journey.getFormForStep(TREATMENT_LIST);
    assertEquals("yes", journeyForm.getHasTreatment());
    assertEquals(1, journeyForm.getTreatments().size());
  }

  @Test
  public void submit_setHasTreatmentsToNoIfEmpty() throws Exception {

    TreatmentListForm journeyForm = journey.getFormForStep(TREATMENT_LIST);
    journeyForm.setHasTreatment("yes");
    journeyForm.setTreatments(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasTreatment", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));

    // Then has reset to no.
    assertEquals(
        "no", ((TreatmentListForm) journey.getFormForStep(TREATMENT_LIST)).getHasTreatment());
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
                "hasTreatment", "NotNull"));
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
