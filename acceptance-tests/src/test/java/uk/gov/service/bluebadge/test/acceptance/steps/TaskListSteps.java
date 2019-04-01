package uk.gov.service.bluebadge.test.acceptance.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;

public class TaskListSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public TaskListSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @Then("^I see the \"([^\"]*)\" task list page$")
  public void iSeeTheTaskListPage(String taskListType) throws Throwable {
    iSeeTheTaskListPage(taskListType, null);
  }

  @Then("^I see the \"([^\"]*)\" task list page as (Child|Adult)$")
  public void iSeeTheTaskListPage(String taskListType, String ageGroup) throws Throwable {
    commonSteps.thenIShouldSeePageTitled("Apply for a Blue Badge - GOV.UK");
    commonSteps.thenIShouldSeeTheContent("Check before you start");

    Boolean isAdult = "Adult".equalsIgnoreCase(ageGroup);
    switch (taskListType) {
      case "AFRFCS":
        iSeeAFCSTaskList(isAdult);
        break;
    }
  }

  public void iSeeAFCSTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Enter personal details");
    commonSteps.thenISeeLinkWithText("Prove identity");
    if (isAdult) {
      commonSteps.thenISeeLinkWithText("Prove address");
    }
    commonSteps.thenISeeLinkWithText("Add a photo of yourself");
  }

  @And("^I see task \"([^\"]*)\" as (COMPLETED|IN_PROGRESS|NOT_STARTED)$")
  public void iSeeTaskAs(String taskLink, String taskStatus) throws Throwable {
    commonSteps.thenISeeLinkWithText(taskLink);
    // TODO check the status of the task
    commonSteps.thenIShouldSeeTheContent(taskStatus);
  }
}
