package uk.gov.service.bluebadge.test.acceptance.steps;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.FEEDBACK_URL;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.GOOGLE_ANALYTICS_TAG;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.SitePage;

public class SiteSteps extends AbstractSpringSteps {

  private static final String ATTRIBUTE_VALUE = "value";
  private static final String TAG_INPUT = "input";

  private SitePage sitePage;

  @Autowired
  public SiteSteps(SitePage sitePage) {
    this.sitePage = sitePage;
  }

  @Given(
      "I complete application up to the Main Reason page for \"(yourself|someone else)\" in \"(england|scotland|wales)\"")
  public void givenICompleteApplicationUpToTheMainReasonPageInCountry(
      String myselfOrOther, String country) {
    // default to england
    String council = "Worcester";
    String fullCouncil = "Worcester city council";
    if ("scotland".equalsIgnoreCase(country)) {
      council = "Aberdeenshire";
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      council = "Anglesey";
      fullCouncil = "Isle of Anglesey county council";
    } else if ("ireland".equalsIgnoreCase(country)) {

    }

    // Applicant page
    givenINavigateToPage("applicant");
    if (myselfOrOther.equalsIgnoreCase("yourself")) {
      andICanClickOnElement("applicantType.label.YOURSELF");
    } else if (myselfOrOther.equalsIgnoreCase("someone else")) {
      andICanClickOnElement("applicantType.label.SOMEONE_ELSE");
    }
    whenIClickOn("Continue");
    // Council page
    whenItypeTextForFieldUiPath(council, "councilShortCode");
    iSelectFromAutosuggestCouncilList(fullCouncil);
    whenIClickOn("Continue");
    // Your Authority page
    whenIClickOn("Continue");
    // Receive Benefit page
    iSelectAnOption("benefitType.NONE");
    whenIClickOn("Continue");
    // Main Reason Page
  }

  @Given("I complete application up to the Main Reason page for \"(yourself|someone else)\"")
  public void givenICompleteApplicationUpToTheMainReasonPage(String myselfOrOther) {
    givenICompleteApplicationUpToTheMainReasonPageInCountry(myselfOrOther, "england");
  }

  @Given("^I navigate to (?:the )?\"([^\"]+)\" (?:.* )?page$")
  public void givenINavigateToPage(String pageName) {
    sitePage.openByPageName(pageName);
  }

  @When("^I (?:can )?click on(?: the| link)? \"([^\"]+)\"(?: link| button)?$")
  public void whenIClickOn(String linkTitle) {
    sitePage.findElementWithText(linkTitle).click();
  }

  @Then("^I (?:can )?see \"([^\"]+)\" (?:link|button|image)$")
  public void thenISeeLink(String linkTitle) {
    assertNotNull("Can see element", sitePage.findElementWithTitle(linkTitle));
  }

  @Then("^I (?:can )?see labelled element \"([^\"]+)\" with content \"([^\"]+)\"$")
  public void thenISeeElementWithUiPathAndContent(String uiPath, String content) {
    assertNotNull(
        "Can see element with data-uipath: " + uiPath, sitePage.findElementWithUiPath(uiPath));
    assertThat(sitePage.findElementWithUiPath(uiPath).getText(), containsString(content));
  }

  @Then("^I should see (?:.* )?page titled \"([^\"]+)\"$")
  public void thenIShouldSeePageTitled(String pageTitle) {
    assertThat("I should see page titled.", sitePage.getDocumentTitle(), is(pageTitle));
  }

  @Then("^I should see (?:.* )?page titled \"([^\"]+)\" with GOV.UK suffix")
  public void thenIShouldSeePageTitledWithGovUkSuffix(String pageTitle) {
    assertThat(
        "I should see page titled.",
        sitePage.getDocumentTitle(),
        is(pageTitle + " - GOV.UK Apply for a Blue Badge"));
  }

  @Then("^I should see the content \"([^\"]*)\"$")
  public void thenIShouldSeeTheContent(String content) {
    assertThat(sitePage.getPageContent(), containsString(content));
  }

  @Then("^I should see the \"page not found\" error page$")
  public void thenIShouldSeeThePageNotFoundErrorPage() {
    // Ideally we would check the HTTP response code is 404 as well but it's not
    // currently possible to do this with the Selinium Web Driver.
    // See https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/141
    thenIShouldSeePageTitled("Page not found");
  }

  @Then("^I should (?:also )?see:?$")
  public void thenIShouldAlsoSee(final DataTable pageSections) {
    String elementName;
    for (List<String> elementsContent : pageSections.raw()) {
      elementName = elementsContent.get(0);
      WebElement pageElement = sitePage.findPageElement(elementName);

      assertThat("I should find page element: " + elementName, pageElement, is(notNullValue()));

      assertThat(
          "Page element " + elementName + " should contain ...",
          getElementText(pageElement),
          getMatcherForText(elementsContent.get(1)));
    }
  }

  @Then("^I should(?: also)? see \"([^\"]+)\" with:")
  public void thenIShouldSeeItemsOf(String pageElementName, final DataTable elementItems) {
    WebElement pageElement = sitePage.findPageElement(pageElementName);

    assertNotNull("I should find page element: " + pageElementName, pageElement);

    for (List<String> elementItem : elementItems.raw()) {
      String expectedItemText = elementItem.get(0);

      assertThat(
          "Page element " + pageElementName + " contain item with text: " + expectedItemText,
          getElementTextList(pageElement),
          hasItem(getMatcherForText(expectedItemText)));
    }
  }

  @Then("^I should not see headers?:$")
  public void thenIShouldNotSeeHeaders(DataTable headersTable) {
    List<String> headers = headersTable.asList(String.class);
    for (String header : headers) {
      assertNull("Header should not be displayed", sitePage.findElementWithText(header));
    }
  }

  @Then("^I should see headers?:$")
  public void thenIShouldSeeHeaders(DataTable headersTable) {
    List<String> headers = headersTable.asList(String.class);
    for (String header : headers) {
      assertNotNull("Header should be displayed: " + header, sitePage.findElementWithText(header));
    }
  }

  @SuppressWarnings("squid:S2925")
  @Then("^I wait (\\d+)s$")
  public void thenIWait(int sec) throws InterruptedException {
    Thread.sleep(sec * 1000L);
  }

  private static Matcher<String> getMatcherForText(String text) {
    if (text.endsWith(" ...")) {
      return startsWith(text.substring(0, text.length() - 4));
    }

    return is(text);
  }

  private String getElementText(WebElement element) {
    if (element.getTagName().equals(TAG_INPUT)) {
      return element.getAttribute(ATTRIBUTE_VALUE);
    }

    return element.getText();
  }

  private List<String> getElementTextList(WebElement element) {
    return element
        .findElements(By.tagName("li"))
        .stream()
        .map(WebElement::getText)
        .collect(toList());
  }

  @Then("^I should not see element with title \"([^\"]*)\"$")
  public void thenIShouldNotSeeElementTitled(String title) {
    assertNull("Element is not on page", sitePage.findElementWithTitle(title));
  }

  @Then("^I should see the title \"([^\"]*)\"$")
  public void iShouldSeeTheTitle(String title) {
    assertThat("Incorrect page title", sitePage.getH1Tag(), getMatcherForText(title));
  }

  @When("^I click on Start now button$")
  public void iClickOnStartNowButton() {
    sitePage.findPageElementById("get-started");
  }

  @And("^I select an option \"([^\"]*)\"$")
  public void iSelectAnOption(String value) {
    sitePage.findPageElementById(value).click();
  }

  @And("^I select an option \"([^\"]*)\" on \"([^\"]*)\"$")
  public void iSelectAnOption(String value, String selectId) {
    Select select = new Select(sitePage.findPageElementById(selectId));
    select.selectByVisibleText(value);
  }

  @And("^I click on Continue button$")
  public void iClickOnContinueButton() {
    sitePage.findPageElementById("submit").click();
  }

  @And("^I should see error summary box$")
  public void andIshouldSeeErrorSummaryBox() {
    WebElement errorSummaryBox = sitePage.findElementWithUiPath("error-summary-box");
    assertNotNull(errorSummaryBox);
  }

  @And("^I should see \"([^\"]*)\" text on the page$")
  public void iShouldSeeTextOnPage(String content) {
    assertTrue(sitePage.getPageContent().contains(content));
  }

  @And("^I should not see \"([^\"]*)\" text on the page$")
  public void iShouldNotSeeTextOnPage(String content) {
    assertFalse(sitePage.getPageContent().contains(content));
  }

  @And("^I (?:can )?click on element \"([^\"]+)\"(?: link| button)?$")
  public void andICanClickOnElement(String uiPath) {
    sitePage.findElementWithUiPath(uiPath).click();
  }

  @When("^I select option \"([^\"]*)\"$")
  public void iSelectOption(String arg0) {
    sitePage.findElementWithUiPath(arg0).click();
  }

  @And("^I can click \"([^\"]*)\" button$")
  public void iCanClickButton(String uiPath) {
    sitePage.findElementWithUiPath(uiPath).click();
  }

  @When("^I type \"([^\"]+)\" for \"([^\"]+)\" field by id$")
  public void whenItypeTextForFieldUiPath(String text, String fieldId) {
    sitePage.findPageElementById(fieldId).sendKeys(text);
  }

  @And("^I select \"([^\"]*)\" from autosuggest council list$")
  public void iSelectFromAutosuggestCouncilList(String arg0) {
    sitePage.selectLocalCouncil(arg0);
  }

  @When(
      "^I type day as \"([^\"]*)\" month as \"([^\"]*)\" and year as \"([^\"]*)\" for applicant's date of birth$")
  public void iTypeDayAsMonthAsAndYearAsForApplicantSDateOfBirth(
      String day, String month, String year) throws Throwable {
    sitePage.findElementWithUiPath("dateOfBirth.day.field").sendKeys(day);
    sitePage.findElementWithUiPath("dateOfBirth.month.field").sendKeys(month);
    sitePage.findElementWithUiPath("dateOfBirth.year.field").sendKeys(year);
  }

  @Then(
      "^I should see \"(You're|They are|You may be|They may be|You're not|They're not)\" eligible page$")
  public void iShouldSeeEligiblePage(String who) throws Throwable {
    String page_title = who + " eligible for a Blue Badge - GOV.UK Apply for a Blue Badge";

    if (who.equals("You're not") || who.equals("They're not")) {
      page_title = who + " eligible - GOV.UK Apply for a Blue Badge";
    }

    assertThat("I should see page titled.", sitePage.getDocumentTitle(), is(page_title));
  }

  @Then("^I can see feedback link$")
  public void iCanSeeFeedbackLink() throws Throwable {
    assertTrue(FEEDBACK_URL.equals(sitePage.findElementWithText("feedback").getAttribute("href")));
  }

  @Then("^The google analytics tag should be available$")
  public void theGoogleAnalyticsTagShouldBeAvailable() throws Throwable {
    assertTrue(sitePage.getPageContent().contains(GOOGLE_ANALYTICS_TAG));
  }
}


