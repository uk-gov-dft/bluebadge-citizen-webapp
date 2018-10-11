package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

public class HealthcareProfessionalListControllerTest
    extends ControllerTestFixture<HealthcareProfessionalListController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new HealthcareProfessionalListController(mockRouteMaster));
    journey.setHealthcareProfessionalListForm(
        HealthcareProfessionalListForm.builder()
            .healthcareProfessionals(new ArrayList<>())
            .build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
  }

  @Override
  String getTemplateName() {
    return "healthcare-professional-list";
  }

  @Override
  String getUrl() {
    return "/list-healthcare-professionals";
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

    journey.getHealthcareProfessionalListForm().setHasHealthcareProfessional("yes");
    journey
        .getHealthcareProfessionalListForm()
        .setHealthcareProfessionals(
            Lists.newArrayList(
                HealthcareProfessionalAddForm.builder()
                    .healthcareProfessionalName("Dr No")
                    .healthcareProfessionalLocation("Island")
                    .build()));

    mockMvc
        .perform(
            post(getUrl())
                .param("hasHealthcareProfessional", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    assertEquals("yes", journey.getHealthcareProfessionalListForm().getHasHealthcareProfessional());
    assertEquals(
        1, journey.getHealthcareProfessionalListForm().getHealthcareProfessionals().size());
  }

  @Test
  public void submit_setHasProfessionalsToNoIfEmpty() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(HealthcareProfessionalListForm.class)))
        .thenReturn("redirect:/testSuccess");

    journey.getHealthcareProfessionalListForm().setHasHealthcareProfessional("yes");
    journey.getHealthcareProfessionalListForm().setHealthcareProfessionals(new ArrayList<>());
    mockMvc
        .perform(
            post(getUrl())
                .param("hasHealthcareProfessional", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
    // Then has reset to no.
    assertEquals("no", journey.getHealthcareProfessionalListForm().getHasHealthcareProfessional());
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
