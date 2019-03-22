package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.APPLICANT_TYPE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.DECLARATIONS;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.RECEIVE_BENEFITS;

import java.util.Optional;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

public class RouteMasterTest {
  private RouteMaster routeMaster;

  @Before
  public void setup() {
    routeMaster = RouteMasterFixture.routeMaster();
  }

  @Test
  public void redirectOnSuccessWithForm_singleNextStep() {
    StepForm testForm =
        new StepForm() {
          @Override
          public StepDefinition getAssociatedStep() {
            return APPLICANT_TYPE;
          }

          @Override
          public boolean preserveStep(Journey journey) {
            return false;
          }
        };

    assertThat(routeMaster.redirectToOnSuccess(testForm, new Journey()))
        .isEqualTo("redirect:" + Mappings.URL_CHOOSE_YOUR_COUNCIL);
  }

  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccessWithForm_whenMultiple_thenException() {
    StepForm testForm =
        new StepForm() {
          @Override
          public StepDefinition getAssociatedStep() {
            return RECEIVE_BENEFITS;
          }

          @Override
          public boolean preserveStep(Journey journey) {
            return false;
          }
        };

    routeMaster.redirectToOnSuccess(testForm, new Journey());
  }

  @Test
  public void redirectOnSuccessWithForm_whenMultiple_thenFormDetermines() {
    StepForm testForm =
        new StepForm() {
          @Override
          public StepDefinition getAssociatedStep() {
            return RECEIVE_BENEFITS;
          }

          @Override
          public Optional<StepDefinition> determineNextStep(Journey j) {
            return Optional.of(ELIGIBLE);
          }

          @Override
          public boolean preserveStep(Journey journey) {
            return false;
          }
        };

    assertThat(routeMaster.redirectToOnSuccess(testForm, new Journey()))
        .isEqualTo("redirect:" + Mappings.URL_ELIGIBLE);
  }

  @Test(expected = IllegalStateException.class)
  @Ignore // Route master to throw an exception proves a config error.
  public void redirectOnSuccessWithForm_whenMultipleAndFormDeterminesInvalid_thenException() {
    StepForm testForm =
        new StepForm() {
          @Override
          public StepDefinition getAssociatedStep() {
            return RECEIVE_BENEFITS;
          }

          @Override
          public Optional<StepDefinition> determineNextStep(Journey j) {
            return Optional.of(DECLARATIONS);
          }

          @Override
          public boolean preserveStep(Journey journey) {
            return false;
          }
        };

    routeMaster.redirectToOnSuccess(testForm, new Journey());
  }

  @Test
  public void isValidState_general() {
    // A valid journey
    Journey journey =
        new JourneyBuilder()
            .toStep(StepDefinition.DECLARATIONS)
            .withEligibility(EligibilityCodeField.WALKD)
            .build();
    assertThat(routeMaster.isValidState(StepDefinition.DECLARATIONS, journey)).isTrue();

    // Remove a step
    journey.setFormForStep(
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.AFRFCS).build());
    assertThat(routeMaster.isValidState(StepDefinition.DECLARATIONS, journey)).isFalse();
    assertThat(routeMaster.isValidState(StepDefinition.CONTACT_DETAILS, journey)).isFalse();
    // First few steps not removed.
    assertThat(routeMaster.isValidState(StepDefinition.YOUR_ISSUING_AUTHORITY, journey)).isTrue();
  }

  @Test
  public void isValidState_firstStep() {
    // These steps are always valid;
    Journey journey = new JourneyBuilder().toStep(StepDefinition.HOME).build();
    assertThat(routeMaster.isValidState(StepDefinition.HOME, journey)).isTrue();
    assertThat(routeMaster.isValidState(StepDefinition.APPLICANT_TYPE, journey)).isTrue();
  }
}
