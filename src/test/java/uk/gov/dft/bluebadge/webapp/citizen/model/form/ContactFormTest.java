package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

@Slf4j
public class ContactFormTest {
  @BeforeEach
  void beforeEachTest(TestInfo testInfo) {
    log.info(String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  void afterEachTest(TestInfo testInfo) {
    log.info(String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"PIP", "DLA", "AFRFCS", "WPMS", "BLIND"})
  @DisplayName(
      "Should skip `health conditions` step in case if PIP, DLA, AFRFCS, WPMS, or BLIND benefit type was selected")
  public void submit_shouldSkipHealthConditionsStep(String input) throws Exception {

    Journey journey = JourneyFixture.getDefaultJourney();
    ReceiveBenefitsForm benefitsForm =
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.valueOf(input)).build();
    journey.setReceiveBenefitsForm(benefitsForm);
    ContactDetailsForm contactForm = ContactDetailsForm.builder().build();

    assertTrue(contactForm.determineNextStep(journey).isPresent());
    assertEquals(contactForm.determineNextStep(journey).get(), StepDefinition.DECLARATIONS);
  }

  @Test
  @DisplayName(
      "Should show `health conditions` step in case if benefit code selected is different from PIP, DLA, AFCS, WPMS, or BLIND")
  public void submit_shouldShowHealthConditionsStep() throws Exception {

    Journey journey = JourneyFixture.getDefaultJourney();
    ReceiveBenefitsForm benefitsForm = ReceiveBenefitsForm.builder().build();
    journey.setReceiveBenefitsForm(benefitsForm);

    MainReasonForm mainReasonForm =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.ARMS).build();
    journey.setMainReasonForm(mainReasonForm);

    ContactDetailsForm contactForm = ContactDetailsForm.builder().build();

    assertTrue(contactForm.determineNextStep(journey).isPresent());
    assertEquals(contactForm.determineNextStep(journey).get(), StepDefinition.HEALTH_CONDITIONS);
  }
}
