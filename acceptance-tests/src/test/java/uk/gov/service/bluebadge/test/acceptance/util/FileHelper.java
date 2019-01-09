package uk.gov.service.bluebadge.test.acceptance.util;

import static java.nio.file.Files.readAllBytes;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FileHelper {

  public static Path createFile(final Path location, final String name, final byte[] content) {
    try {
      Files.createDirectories(location);

      final Path tempFile = Files.createFile(Paths.get(location.toString(), name));

      Files.write(tempFile, content);

      return tempFile;

    } catch (final IOException exception) {
      throw new UncheckedIOException(exception);
    }
  }

  public static byte[] readFileAsByteArray(final Path path) {
    try {
      return readAllBytes(path);
    } catch (final IOException exception) {
      throw new UncheckedIOException(exception);
    }
  }

  /**
   * Blocks current thread until the given file appears. Throws an exception if the file doesn't
   * appear within 10 seconds.
   */
  public static void waitUntilFileAppears(final Path path) {
    FileUtils.waitFor(path.toFile(), 10);
    if (Files.notExists(path)) {
      throw new RuntimeException(
          "File did not appear within the defined timeout: " + path.toString());
    }
  }

  public static String toHumanFriendlyFileSize(final long bytesCount) {

    String formattedFileSize = "";

    final int unit = 1000;
    if (bytesCount < unit) {
      formattedFileSize = bytesCount + " B";
    } else {
      final int exp = (int) (Math.log(bytesCount) / Math.log(unit));
      final String pre = String.valueOf(("kMGTPE").charAt(exp - 1));
      formattedFileSize = String.format("%.1f %sB", bytesCount / Math.pow(unit, exp), pre);
    }

    return formattedFileSize;
  }

  public static void dropFile(File filePath, WebElement target, int offsetX, int offsetY) {
    if (!filePath.exists()) throw new WebDriverException("File not found: " + filePath.toString());

    WebDriver driver = ((RemoteWebElement) target).getWrappedDriver();
    JavascriptExecutor jse = (JavascriptExecutor) driver;
    WebDriverWait wait = new WebDriverWait(driver, 30);

    String JS_DROP_FILE =
        "var target = arguments[0],"
            + "    offsetX = arguments[1],"
            + "    offsetY = arguments[2],"
            + "    document = target.ownerDocument || document,"
            + "    window = document.defaultView || window;"
            + ""
            + "var input = document.createElement('INPUT');"
            + "input.type = 'file';"
            + "input.style.display = 'none';"
            + "input.onchange = function () {"
            + "  var rect = target.getBoundingClientRect(),"
            + "      x = rect.left + (offsetX || (rect.width >> 1)),"
            + "      y = rect.top + (offsetY || (rect.height >> 1)),"
            + "      dataTransfer = { files: this.files };"
            + ""
            + "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {"
            + "    var evt = document.createEvent('MouseEvent');"
            + "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"
            + "    evt.dataTransfer = dataTransfer;"
            + "    target.dispatchEvent(evt);"
            + "  });"
            + ""
            + "  setTimeout(function () { document.body.removeChild(input); }, 25);"
            + "};"
            + "document.body.appendChild(input);"
            + "return input;";

    WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
    input.sendKeys(filePath.getAbsoluteFile().toString());
    wait.until(ExpectedConditions.stalenessOf(input));
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("dft-fu-preview"))));
  }
}
