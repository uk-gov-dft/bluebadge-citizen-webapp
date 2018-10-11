package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

public class TreatmentListControllerTest extends ControllerTestFixture<TreatmentListController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new TreatmentListController(mockRouteMaster));
    journey.setTreatmentListForm(TreatmentListForm.builder().treatments(new ArrayList<>()).build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
  }

  @Override
  String getTemplateName() {
    return "treatment-list";
  }

  @Override
  String getUrl() {
    return "/list-treatments";
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
    when(mockRouteMaster.redirectToOnSuccess(any(TreatmentListForm.class)))
        .thenReturn("redirect:/testSuccess");
    mockMvc
        .perform(
            post(getUrl())
                .param("hasTreatment", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney_withTreatments() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(TreatmentListForm.class)))
        .thenReturn("redirect:/testSuccess");

    List<TreatmentAddForm> treatments = new ArrayList<>();
    TreatmentAddForm treatment = new TreatmentAddForm();
    treatment.setTreatmentWhen("F");
    treatment.setTreatmentDescription("A");
    treatments.add(treatment);
    journey.getTreatmentListForm().setHasTreatment("yes");
    journey.getTreatmentListForm().setTreatments(treatments);

    mockMvc
        .perform(
            post(getUrl())
                .param("hasTreatment", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    assertEquals("yes", journey.getTreatmentListForm().getHasTreatment());
    assertEquals(1, journey.getTreatmentListForm().getTreatments().size());
  }

  @Test
  public void submit_setHasTreatmentsToNoIfEmpty() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(TreatmentListForm.class)))
        .thenReturn("redirect:/testSuccess");

    journey.getTreatmentListForm().setHasTreatment("yes");
    journey.getTreatmentListForm().setTreatments(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasTreatment", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
    // Then has reset to no.
    assertEquals("no", journey.getTreatmentListForm().getHasTreatment());
  }

  @Test
  public void submit_bindingError() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(TreatmentListForm.class)))
        .thenReturn("redirect:/testSuccess");

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
