package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;

public class MobilityAidListControllerTest {
  private MockMvc mockMvc;
  private Journey journey;
  private static final String SUCCESS_URL = Mappings.URL_WALKING_TIME;

  @Before
  public void setup() {
    MobilityAidListController controller =
        new MobilityAidListController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();

    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.MOBILITY_AID_LIST, EligibilityCodeField.WALKD);
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
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post("/list-mobility-aids")
                .param("hasWalkingAid", "no")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_setHasAidsToNoIfEmpty() throws Exception {

    MobilityAidListForm journeyListForm = journey.getFormForStep(StepDefinition.MOBILITY_AID_LIST);
    journeyListForm.setHasWalkingAid("yes");
    journeyListForm.setMobilityAids(new ArrayList<>());
    mockMvc
        .perform(
            post("/list-mobility-aids")
                .param("hasWalkingAid", "yes")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
    // Then has aids reset to no. (need to get back out of journey as new object.)
    journeyListForm = journey.getFormForStep(StepDefinition.MOBILITY_AID_LIST);
    assertEquals("no", journeyListForm.getHasWalkingAid());
  }

  @Test
  public void submit_bindingError() throws Exception {

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
