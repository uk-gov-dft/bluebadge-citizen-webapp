package uk.gov.service.bluebadge.test.acceptance.steps;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.*;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AlreadyHaveBlueBadgePage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;

public class CommonSteps extends AbstractSpringSteps {

  private static final String ATTRIBUTE_VALUE = "value";
  private static final String TAG_INPUT = "input";

  private CommonPage commonPage;
  private String pageURL;

  @Autowired
  public CommonSteps(CommonPage commonPage) {
    this.commonPage = commonPage;
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
      andICanClickOnElementUiPath("applicantType.label.YOURSELF");
    } else if (myselfOrOther.equalsIgnoreCase("someone else")) {
      andICanClickOnElementUiPath("applicantType.label.SOMEONE_ELSE");
    }
    whenIClickOn("Continue");
    // Council page
    whenItypeTextForFieldUiPath(council, "councilShortCode");
    iSelectFromAutosuggestCouncilList(fullCouncil);
    whenIClickOn("Continue");
    // Your Authority page
    whenIClickOn("Continue");
    // Existing Badge page
    iCompleteTheAlreadyHaveABlueBadgePage("YES");
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
    commonPage.openByPageName(pageName);
  }

  @When("^I (?:can )?click on(?: the| link)? \"([^\"]+)\"(?: link| button)?$")
  public void whenIClickOn(String linkTitle) {
    commonPage.findElementWithText(linkTitle).click();
  }

  @Then("^I (?:can )?see \"([^\"]+)\" (?:link|button|image)$")
  public void thenISeeLink(String linkTitle) {
    assertNotNull("Can see element", commonPage.findElementWithTitle(linkTitle));
  }

  @Then("^I (?:can )?see labelled element \"([^\"]+)\" with content \"([^\"]+)\"$")
  public void thenISeeElementWithUiPathAndContent(String uiPath, String content) {
    assertNotNull(
        "Can see element with data-uipath: " + uiPath, commonPage.findElementWithUiPath(uiPath));
    assertThat(commonPage.findElementWithUiPath(uiPath).getText(), containsString(content));
  }

  @Then("^I should see (?:.* )?page titled \"([^\"]+)\"$")
  public void thenIShouldSeePageTitled(String pageTitle) {
    assertThat("I should see page titled.", commonPage.getDocumentTitle(), is(pageTitle));
  }

  @Then("^I should see (?:.* )?page titled \"([^\"]+)\" with GOV.UK suffix")
  public void thenIShouldSeePageTitledWithGovUkSuffix(String pageTitle) {
    assertThat(
        "I should see page titled.",
        commonPage.getDocumentTitle(),
        is(pageTitle + " - GOV.UK Apply for a Blue Badge"));
  }

  @Then("^I should see the content \"([^\"]*)\"$")
  public void thenIShouldSeeTheContent(String content) {
    assertThat(commonPage.getPageContent(), containsString(content));
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
      WebElement pageElement = commonPage.findPageElement(elementName);

      assertThat("I should find page element: " + elementName, pageElement, is(notNullValue()));

      assertThat(
          "Page element " + elementName + " should contain ...",
          getElementText(pageElement),
          getMatcherForText(elementsContent.get(1)));
    }
  }

  @Then("^I should(?: also)? see \"([^\"]+)\" with:")
  public void thenIShouldSeeItemsOf(String pageElementName, final DataTable elementItems) {
    WebElement pageElement = commonPage.findPageElement(pageElementName);

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
      assertNull("Header should not be displayed", commonPage.findElementWithText(header));
    }
  }

  @Then("^I should see headers?:$")
  public void thenIShouldSeeHeaders(DataTable headersTable) {
    List<String> headers = headersTable.asList(String.class);
    for (String header : headers) {
      assertNotNull(
          "Header should be displayed: " + header, commonPage.findElementWithText(header));
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
    assertNull("Element is not on page", commonPage.findElementWithTitle(title));
  }

  @Then("^I should see the title \"([^\"]*)\"$")
  public void iShouldSeeTheHeading(String title) {
    assertThat("Incorrect page title", commonPage.getH1Tag(), getMatcherForText(title));
  }

  @When("^I click on Start now button$")
  public void iClickOnStartNowButton() {
    commonPage.findPageElementById("get-started");
  }

  @And("^I select an option \"([^\"]*)\"$")
  public void iSelectAnOption(String value) {
    commonPage.findPageElementById(value).click();
  }

  @And("^I select an option \"([^\"]*)\" on \"([^\"]*)\"$")
  public void iSelectAnOption(String value, String selectId) {
    Select select = new Select(commonPage.findPageElementById(selectId));
    select.selectByVisibleText(value);
  }

  @And("^I click on Continue button$")
  public void iClickOnContinueButton() {
    commonPage.findElementWithUiPath("button.continue").click();
  }

  @And("^I should see error summary box$")
  public void andIshouldSeeErrorSummaryBox() {
    WebElement errorSummaryBox = commonPage.findElementWithUiPath("error-summary-box");
    assertNotNull(errorSummaryBox);
  }

  @And("^I should see \"([^\"]*)\" text on the page$")
  public void iShouldSeeTextOnPage(String content) {
    String s = commonPage.getPageContent();
    assertTrue(commonPage.getPageContent().contains(content));
  }

  @And("^I should not see \"([^\"]*)\" text on the page$")
  public void iShouldNotSeeTextOnPage(String content) {
    assertFalse(commonPage.getPageContent().contains(content));
  }

  @And("^I (?:can )?click on element \"([^\"]+)\"(?: link| button)?$")
  public void andICanClickOnElementUiPath(String uiPath) {
    commonPage.findElementWithUiPath(uiPath).click();
  }

  @When("^I select option \"([^\"]*)\"$")
  public void iSelectOption(String arg0) {
    commonPage.findElementWithUiPath(arg0).click();
  }

  @And("^I can click \"([^\"]*)\" button$")
  public void iCanClickButton(String uiPath) {
    commonPage.findElementWithUiPath(uiPath).click();
  }

  @When("^I type \"([^\"]+)\" for \"([^\"]+)\" field by id$")
  public void whenItypeTextForFieldUiPath(String text, String fieldId) {
    commonPage.findPageElementById(fieldId).sendKeys(text);
  }

  @And("^I select \"([^\"]*)\" from autosuggest council list$")
  public void iSelectFromAutosuggestCouncilList(String arg0) {
    commonPage.selectLocalCouncil(arg0);
  }

  @When(
      "^I type day as \"([^\"]*)\" month as \"([^\"]*)\" and year as \"([^\"]*)\" for applicant's date of birth$")
  public void iTypeDayAsMonthAsAndYearAsForApplicantSDateOfBirth(
      String day, String month, String year) {
    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys(day);
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys(month);
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys(year);
  }

  @Then(
      "^I should see \"(You're|They are|You may be|They may be|You're not|They're not)\" eligible page$")
  public void iShouldSeeEligiblePage(String who) {
    String page_title = who + " eligible for a Blue Badge - GOV.UK Apply for a Blue Badge";

    if (who.equals("You're not") || who.equals("They're not")) {
      page_title = who + " eligible - GOV.UK Apply for a Blue Badge";
    }

    assertThat("I should see page titled.", commonPage.getDocumentTitle(), is(page_title));
  }

  @Then("^I can see feedback link$")
  public void iCanSeeFeedbackLink() {
    assertTrue(
        FEEDBACK_URL.equals(commonPage.findElementWithText("feedback").getAttribute("href")));
  }

  @And("^I can see the continue button with link to external welsh site$")
  public void iCanSeeTheContinueButtonWithLinkToExternalWelshSite() {
    assertTrue(
        APPLY_IN_WELSH_EXTERNAL_URL.equals(
            commonPage.findElementWithText("Continue").getAttribute("href")));
  }

  @Then("^The google analytics tag should be available$")
  public void theGoogleAnalyticsTagShouldBeAvailable() {
    assertTrue(commonPage.getPageContent().contains(GOOGLE_ANALYTICS_TAG));
  }

  @And(
      "^I should see the \"(yourself|someone else)\" option button is selected in the Who are you applying for? page$")
  public void iShouldSeeTheOptionButtonIsSelectedInTheWhoAreYouApplyingForPage(String selfOrOther) {

    if (selfOrOther.equalsIgnoreCase("yourself")) {

      assertTrue(commonPage.findPageElementById("applicantType").isSelected());

    } else if (selfOrOther.equalsIgnoreCase("someone else")) {

      assertTrue(commonPage.findPageElementById("applicantType.SOMEONE_ELSE").isSelected());
    }
  }

  @And("^I verify validation message \"([^\"]*)\" \"$")
  public void iVerifyValidationMessage(String message) {
    this.iClickOnContinueButton();
    this.andIshouldSeeErrorSummaryBox();
    this.iShouldSeeTextOnPage(message);
  }

  @And("^I verify multiple validation messages \"([^\"]*)\" \"$")
  public void iVerifyMultipleValidationMessages(List<String> messages) {

    this.iClickOnContinueButton();
    this.andIshouldSeeErrorSummaryBox();

    for (String message : messages) {
      this.iShouldSeeTextOnPage(message);
    }
  }

  @And("^I complete the already have a blue badge page \"(YES|NO|YES BUT DON'T KNOW)\"$")
  public void iCompleteTheAlreadyHaveABlueBadgePage(String opt) {
    if ("YES BUT DON't KNOW".equals(opt)) {
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION).click();
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER_BYPASS_LINK).click();
    } else if ("YES".equals(opt)) {
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION).click();
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER).sendKeys("AB12CD");
    } else {
      commonPage
          .findPageElementById(
              AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION + "_" + opt.toLowerCase())
          .click();
    }
  }

  @Then("^I should see the correct URL$")
  public void iShouldSeeTheCorrectURL(String expectedURL) {
    URL fullURL = null;

    try {

      fullURL = new URL(commonPage.getPageURL());
      assertThat(fullURL.getPath(), is(expectedURL));

    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
}
