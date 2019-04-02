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
    iSeeTheTaskListPage(taskListType, "Adult"); // TODO Prove address shown by default??
  }

  @Then("^I see the \"([^\"]*)\" task list page as (Child|Adult)$")
  public void iSeeTheTaskListPage(String taskListType, String ageGroup) throws Throwable {
    commonSteps.thenIShouldSeePageTitled("Apply for a Blue Badge - GOV.UK");
    commonSteps.thenIShouldSeeTheContent("Check before you start");

    Boolean isAdult = "Adult".equalsIgnoreCase(ageGroup);
    if("CHILDBULK".equals(taskListType) || "CHILDVEHIC".equals(taskListType)){
      isAdult = false;
    }

    iSeeCommonTaskList(isAdult);
    switch (taskListType) {
      case "AFRFCS":
      case "WPMS":
        break;
      case "PIP":
      case "DLA":
        iSeeDLAorPIPTaskList(isAdult);
        break;
      case "ARMS":
        iSeeArmsTaskList(isAdult);
        break;
      case "BLIND":
        iSeeBlindTaskList(isAdult);
        break;
      case "CHILDBULK":
        iSeeChildBulkEquipTaskList(isAdult);
        break;
      case "CHILDVEHIC":
        iSeeChildVehicleTaskList(isAdult);
        break;
      case "WALKD":
        iSeeWalkingTaskList(isAdult);
        break;
    }
  }

  public void iSeeCommonTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Enter personal details");
    commonSteps.thenISeeLinkWithText("Prove identity");
    if (isAdult) {
      commonSteps.thenISeeLinkWithText("Prove address");
    } else {
      commonSteps.thenIDontSeeLinkWithText("Prove address");
    }
    commonSteps.thenISeeLinkWithText("Add a photo of yourself");
  }

  public void iSeeDLAorPIPTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Provide proof of benefit");
  }
  public void iSeeArmsTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Describe your condition");
    commonSteps.thenISeeLinkWithText("Add supporting documents");
  }
  public void iSeeBlindTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Provide proof of visual impairment");
  }
  public void iSeeChildBulkEquipTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Describe your condition");
    commonSteps.thenISeeLinkWithText("Add supporting documents");
    commonSteps.thenISeeLinkWithText("List medical equipment");
    commonSteps.thenISeeLinkWithText("List healthcare professionals");
  }
  public void iSeeChildVehicleTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Describe your condition");
    commonSteps.thenISeeLinkWithText("Add supporting documents");
    commonSteps.thenISeeLinkWithText("List healthcare professionals");
  }
  public void iSeeWalkingTaskList(Boolean isAdult) throws Throwable {
    commonSteps.thenISeeLinkWithText("Describe walking ability");
    commonSteps.thenISeeLinkWithText("Add supporting documents");
    commonSteps.thenISeeLinkWithText("List medication");
    commonSteps.thenISeeLinkWithText("List treatments");
    commonSteps.thenISeeLinkWithText("List healthcare professionals");
  }

  @And("^I see task \"([^\"]*)\" as (COMPLETED|IN_PROGRESS|NOT_STARTED)$")
  public void iSeeTaskAs(String taskLink, String taskStatus) throws Throwable {
    commonSteps.thenISeeLinkWithText(taskLink);
    // TODO check the status of the task
    commonSteps.thenIShouldSeeTheContent(taskStatus);
  }
}
