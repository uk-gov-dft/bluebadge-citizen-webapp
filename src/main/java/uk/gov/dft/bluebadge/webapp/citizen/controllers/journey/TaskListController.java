package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_TASK_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Controller
@RequestMapping(URL_TASK_LIST)
public class TaskListController implements StepController {
  private static final String TEMPLATE = "task-list";
  private final RouteMaster routeMaster;
  private final JourneySpecification journeySpecification;

  public TaskListController(RouteMaster routeMaster, JourneySpecification journeySpecification) {
    this.routeMaster = routeMaster;
    this.journeySpecification = journeySpecification;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey){

    if(!journeySpecification.getPreApplicationJourney().isComplete(journey)){
      return routeMaster.backToCompletedPrevious(journey);
    }

    JourneySection applicationSection = journeySpecification.getApplicationSection(journey);
    if(null == applicationSection){
      throw new IllegalStateException("Task list page, but no application section found.");
    }

    setupModel(model, journey);

    // name of the section
    // name of the task
    // state of the task. disabled/not started/incomplete/complete/NA
    //    need to ask the section if the task is enabled or not?
    //    Read declaration needs completing before Submit can be done.
    //    Application section needs completing before read declaration is enabled.

    // Removal of proof of address ** data ** if change age??

    return TEMPLATE;
  }

  private void setupModel(Model model, Journey journey){
    JourneySection applicationSection = journeySpecification.getApplicationSection(journey);
    List<TaskView> taskViews = applicationSection.getTasks().stream()
        .filter(t->null != t.getFirstStep(journey))// Hide task if no first step
        .map(t -> TaskView.builder()
            .titleCode(t.getTitleCode(journey))
            .url(Mappings.getUrl(t.getFirstStep(journey)))
            .enabled(true)
            .taskState(t.getState(journey))
            .build()
        )
        .collect(Collectors.toList());
    model.addAttribute("applicationSectionTasks", taskViews);

    boolean previousComplete = applicationSection.isComplete(journey);
    taskViews = new ArrayList<>();
    for(Task t : journeySpecification.getSubmitAndPayJourney().getTasks()){
      taskViews.add(TaskView.builder()
          .titleCode(t.getTitleCode(journey))
          .url(Mappings.getUrl(t.getFirstStep(journey)))
          .enabled(previousComplete)
          .taskState(t.getState(journey))
          .build());
      previousComplete = t.isComplete(journey);
    }
    model.addAttribute("applySectionTasks", taskViews);


  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.TASK_LIST;
  }

  @Builder
  @Getter
  private static class TaskView{
    private final String titleCode;
    private final String url;
    private final boolean enabled;
    private final Task.TaskState taskState;
  }
}
