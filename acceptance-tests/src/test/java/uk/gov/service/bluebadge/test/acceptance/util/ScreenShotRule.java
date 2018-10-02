package uk.gov.service.bluebadge.test.acceptance.util;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenShotRule extends TestWatcher {

  private WebDriver webDriver;

  public ScreenShotRule(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  @Override
  protected void failed(Throwable e, Description description) {
    String methodName = description.getMethodName();
    String fileName = description.getTestClass().getSimpleName() + "-" + methodName + ".png";

    try {
      File destiny = new File(fileName);
      FileUtils.copyFile(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE), destiny);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  @Override
  protected void finished(Description description) {
    webDriver.quit();
  }
}
