package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;

public class HealthcareProfessionalListControllerTest
    extends ControllerTestFixture<HealthcareProfessionalListController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new HealthcareProfessionalListController(mockRouteMaster));
    journey.setFormForStep(
        HealthcareProfessionalListForm.builder()
            .healthcareProfessionals(new ArrayList<>())
            .build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
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
    when(mockRouteMaster.redirectToOnSuccess(any(HealthcareProfessionalListForm.class)))
        .thenReturn("redirect:/testSuccess");
    mockMvc
        .perform(
            post(getUrl())
                .param("hasHealthcareProfessional", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withTreatments() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HealthcareProfessionalListForm.class)))
        .thenReturn("redirect:/testSuccess");

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
        .andExpect(redirectedUrl("/testSuccess"));

    // Get form again.  Will be different instance in journey now
    journeyForm = journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST);
    assertEquals("yes", journeyForm.getHasHealthcareProfessional());
    assertEquals(1, journeyForm.getHealthcareProfessionals().size());
  }

  @Test
  public void submit_setHasProfessionalsToNoIfEmpty() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HealthcareProfessionalListForm.class)))
        .thenReturn("redirect:/testSuccess");

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
        .andExpect(redirectedUrl("/testSuccess"));

    // Then has reset to no.
    assertEquals(
        "no",
        ((HealthcareProfessionalListForm) journey.getFormForStep(HEALTHCARE_PROFESSIONAL_LIST))
            .getHasHealthcareProfessional());
  }

  @Test
  public void submit_bindingError() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HealthcareProfessionalListForm.class)))
        .thenReturn("redirect:/testSuccess");

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
