package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

@Slf4j
@RunWith(JUnitPlatform.class)
public class GenderFormTest {

  @BeforeEach
  void beforeEachTest(TestInfo testInfo) {
    log.info(String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  void afterEachTest(TestInfo testInfo) {
    log.info(String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"PIP", "DLA", "AFCS", "WPMS", "BLIND"})
  @DisplayName(
      "Should skip `health conditions` step in case if PIP, DLA, AFCS, WPMS, or BLIND benefit type was selected")
  public void submit_shouldSkipHealthConditionsStep(String input) throws Exception {

    Journey journey = JourneyFixture.getDefaultJourney();
    ReceiveBenefitsForm benefitsForm =
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.valueOf(input)).build();
    journey.setReceiveBenefitsForm(benefitsForm);
    DateOfBirthForm dobForm =
        DateOfBirthForm.builder().dateOfBirth(new CompoundDate("01", "01", "1980")).build();

    assertTrue(dobForm.determineNextStep(journey).isPresent());
    assertEquals(dobForm.determineNextStep(journey).get(), StepDefinition.DECLARATIONS);
  }
}
