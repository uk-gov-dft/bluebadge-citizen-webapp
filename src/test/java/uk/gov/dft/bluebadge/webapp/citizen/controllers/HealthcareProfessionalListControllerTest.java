package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

public class HealthcareProfessionalListControllerTest
    extends ControllerTestFixture<HealthcareProfessionalListController> {

  @Before
  public void setup() {
    super.setup(new HealthcareProfessionalListController(RouteMasterFixture.routeMaster()));
    journey.setFormForStep(
        HealthcareProfessionalListForm.builder()
            .healthcareProfessionals(new ArrayList<>())
            .build());
  }

  @Override
  protected String getTemplateName() {
    return "healthcare-professional-list";
  }

  @Override
  protected String getUrl() {
    return "/list-healthcare-professionals";
  }

  @Override
  protected StepDefinition getStep() {
    return HEALTHCARE_PROFESSIONAL_LIST;
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
                .param("hasHealthcareProfessional", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withTreatments() throws Exception {

    HealthcareProfessionalAddForm form = new HealthcareProfessionalAddForm();
    form.setHealthcareProfessionalLocation("Island");
    form.setHealthcareProfessionalName("Dr No");

    HealthcareProfessionalListForm journeyForm =
        journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST);
    journeyForm.setHasHealthcareProfessional("yes");
    journeyForm.setHealthcareProfessionals(Lists.newArrayList(form));

    mockMvc
        .perform(
            post(getUrl())
                .param("hasHealthcareProfessional", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));

    // Get form again.  Will be different instance in journey now
    journeyForm = journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST);
    assertEquals("yes", journeyForm.getHasHealthcareProfessional());
    assertEquals(1, journeyForm.getHealthcareProfessionals().size());
  }

  @Test
  public void submit_setHasProfessionalsToNoIfEmpty() throws Exception {

    HealthcareProfessionalListForm journeyForm =
        journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST);
    journeyForm.setHasHealthcareProfessional("yes");
    journeyForm.setHealthcareProfessionals(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasHealthcareProfessional", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));

    // Then has reset to no.
    assertEquals(
        "no",
        ((HealthcareProfessionalListForm) journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST))
            .getHasHealthcareProfessional());
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
                "hasHealthcareProfessional", "NotNull"));
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
