package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;

import java.util.EnumSet;
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
  public void submit_shouldSkipHealthConditionsStep(String input) {

    Journey journey = JourneyFixture.getDefaultJourney();
    EligibilityCodeField benefitType = EligibilityCodeField.valueOf(input);
    ReceiveBenefitsForm benefitsForm =
        ReceiveBenefitsForm.builder().benefitType(benefitType).build();
    journey.setFormForStep(benefitsForm);
    ContactDetailsForm contactForm = ContactDetailsForm.builder().build();

    assertTrue(contactForm.determineNextStep(journey).isPresent());
    if (EnumSet.of(PIP, DLA).contains(benefitType)) {
      assertEquals(StepDefinition.PROVE_BENEFIT, contactForm.determineNextStep(journey).get());
    } else if (BLIND == benefitType) {
      assertEquals(StepDefinition.REGISTERED, contactForm.determineNextStep(journey).get());
    } else {
      assertEquals(StepDefinition.PROVE_IDENTITY, contactForm.determineNextStep(journey).get());
    }
  }

  @Test
  @DisplayName(
      "Should show `health conditions` step in case if benefit code selected is different from PIP, DLA, AFCS, WPMS, or BLIND")
  public void submit_shouldShowHealthConditionsStep() {

    Journey journey = JourneyFixture.getDefaultJourney();
    ReceiveBenefitsForm benefitsForm = ReceiveBenefitsForm.builder().build();
    journey.setFormForStep(benefitsForm);

    MainReasonForm mainReasonForm =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.ARMS).build();
    journey.setFormForStep(mainReasonForm);

    ContactDetailsForm contactForm = ContactDetailsForm.builder().build();

    assertTrue(contactForm.determineNextStep(journey).isPresent());
    assertEquals(StepDefinition.HEALTH_CONDITIONS, contactForm.determineNextStep(journey).get());
  }
}
