package uk.gov.service.bluebadge.test.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/** Entry point required by {@code cucumber-jvm} to discover and run tests. */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/site/testSuite7/")
public class AcceptanceTest7 {
  // no-op, config class only
}
