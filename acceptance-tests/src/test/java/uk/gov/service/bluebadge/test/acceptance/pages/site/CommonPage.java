package uk.gov.service.bluebadge.test.acceptance.pages.site;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.service.bluebadge.test.acceptance.pages.PageHelper;
import uk.gov.service.bluebadge.test.acceptance.util.TestContentUrls;
import uk.gov.service.bluebadge.test.acceptance.webdriver.WebDriverProvider;

public class CommonPage {

  private final WebDriverProvider webDriverProvider;

  private PageHelper helper;
  private TestContentUrls urlLookup;
  private List<PageElements> pagesElements;

  public CommonPage(WebDriverProvider webDriverProvider, final PageHelper helper) {
    this.webDriverProvider = webDriverProvider;
    this.helper = helper;
    this.urlLookup = new TestContentUrls();
    this.pagesElements = new ArrayList<>();

    pagesElements.add(new CommonPageElements());
  }

  private WebDriver getWebDriver() {
    return webDriverProvider.getWebDriver();
  }

  public void openByPageName(final String pageName) {
    try {
      String lookupUrl = urlLookup.lookupUrl(pageName);
      getWebDriver().get(lookupUrl);
    } catch (Exception ex) {
      openByPageNameUnmapped(pageName);
    }
  }

  private void openByPageNameUnmapped(final String pageName) {
    String lookupUrl = TestContentUrls.lookupUrlUnmapped(pageName);
    getWebDriver().get(lookupUrl);
  }

  public WebElement findElementWithText(String text) {
    return helper.findOptionalElement(By.xpath("//*[text()='" + text + "']"));
  }

  public WebElement findElementWithTitle(String title) {
    return helper.findOptionalElement(By.xpath("//*[@title='" + title + "']"));
  }

  public WebElement findElementWithUiPath(String uiPath) {
    return helper.findOptionalElement(By.xpath("//*[@data-uipath='" + uiPath + "']"));
  }

  //Needs to delete this
  public WebElement findElementAddMobilityAid() {
    return helper.findElement(By.xpath("//*[@id=\"conditional-hasWalkingAid\"]/p/a"));
  }

  public void selectLocalCouncil(String textToSelect) {
    selectFromAutoCompleteList("councilShortCode", textToSelect);
  }

  public void selectFromAutoCompleteList(String component, String textToSelect) {
    WebElement autoOptions =
        (new WebDriverWait(getWebDriver(), 10))
            .until(ExpectedConditions.elementToBeClickable(By.id(component + "__listbox")));

    List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
    for (WebElement option : optionsToSelect) {
      if (option.getText().equals(textToSelect)) {
        option.click();
        break;
      }
    }
  }

  public String getDocumentTitle() {
    return getWebDriver().getTitle();
  }

  public WebElement findPageElement(String elementName) {
    return findPageElement(elementName, 0);
  }

  public String getPageContent() {
    return getWebDriver().getPageSource();
  }

  private WebElement findPageElement(String elementName, int nth) {
    for (PageElements pageElements : pagesElements) {
      if (pageElements.contains(elementName)) {
        return pageElements.getElementByName(elementName, nth, helper);
      }
    }

    return null;
  }

  public WebElement findPageElementById(String elementId) {
    return helper.findElement(By.id(elementId));
  }

  public String getH1Tag() {
    return getWebDriver().findElement(By.tagName("h1")).getText();
  }

  public PageHelper getHelper() {
    return helper;
  }
}
