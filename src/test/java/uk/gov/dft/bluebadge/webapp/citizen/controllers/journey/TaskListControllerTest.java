package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

class TaskListControllerTest {
  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    TaskListController controller =
        new TaskListController(
            RouteMasterFixture.routeMaster(), RouteMasterFixture.fullJourneySpec());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  void show_ShouldDisplayTemplate() throws Exception {
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.GENDER, EligibilityCodeField.PIP, false);

    MvcResult mvcResult =
        mockMvc
            .perform(get("/task-list").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("task-list"))
            .andExpect(model().attribute("applicationSectionTasks", Matchers.notNullValue()))
            .andExpect(model().attribute("applySectionTasks", Matchers.notNullValue()))
            .andReturn();

    Map<String, Object> model = mvcResult.getModelAndView().getModel();
    List<TaskListController.TaskView> applicationSectionTasks =
        (List<TaskListController.TaskView>) model.get("applicationSectionTasks");
    assertThat(applicationSectionTasks)
        .extracting("titleCode")
        .containsExactly(
            "taskList.application.section.personalDetails",
            "taskList.application.section.provideProofBenefit",
            "taskList.application.section.proveIdentity",
            "taskList.application.section.proveAddress",
            "taskList.application.section.providePhoto");
    List<TaskListController.TaskView> applySectionTasks =
        (List<TaskListController.TaskView>) model.get("applySectionTasks");
    assertThat(applySectionTasks)
        .extracting("titleCode")
        .containsExactly("taskList.apply.section.declaration", "taskList.apply.section.submit");
  }

  @Test
  void show_whenPreAppNotComplete_thenRedirectToStart() throws Exception {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.YOUR_ISSUING_AUTHORITY);

    mockMvc
        .perform(get("/task-list").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
  }
}
