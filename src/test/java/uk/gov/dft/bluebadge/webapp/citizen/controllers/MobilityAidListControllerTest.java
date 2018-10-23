package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class MobilityAidListControllerTest {
  private MockMvc mockMvc;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    MobilityAidListController controller = new MobilityAidListController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.MOBILITY_AID_LIST, EligibilityCodeField.WALKD);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    // We are not testing the route master. So for convenience just forward to an error view so
    // can test the error messages
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any())).thenCallRealMethod();
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get("/list-mobility-aids").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("mobility-aid-list"))
        .andExpect(model().attributeExists("formRequest"));
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get("/list-mobility-aids").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MobilityAidListForm.class)))
        .thenReturn("redirect:/testSuccess");
    mockMvc
        .perform(
            post("/list-mobility-aids")
                .param("hasWalkingAid", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_setHasAidsToNoIfEmpty() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MobilityAidListForm.class)))
        .thenReturn("redirect:/testSuccess");

    journey.getMobilityAidListForm().setHasWalkingAid("yes");
    journey.getMobilityAidListForm().setMobilityAids(new ArrayList<>());
    mockMvc
        .perform(
            post("/list-mobility-aids")
                .param("hasWalkingAid", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
    // Then has aids reset to no.
    assertEquals("no", journey.getMobilityAidListForm().getHasWalkingAid());
  }

  @Test
  public void submit_bindingError() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(MobilityAidListForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/list-mobility-aids")
                // .param("hasWalkingAid", "")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "hasWalkingAid", "NotNull"));
  }

  @Test
  public void remove() throws Exception {
    mockMvc
        .perform(
            get("/list-mobility-aids/remove")
                .sessionAttr("JOURNEY", new Journey())
                .param("uuid", "1234"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-mobility-aids"));
  }
}
