package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.DECLARATIONS;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HOME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.RECEIVE_BENEFITS;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

public class RouteMasterTest {
  private RouteMaster routeMaster;

  @Before
  public void setup() {
    routeMaster = new RouteMaster();
  }

  @Test
  public void redirectOnSuccessWithForm_singleNextStep() {
    StepForm testForm = () -> HOME;

    assertThat(routeMaster.redirectToOnSuccess(testForm))
        .isEqualTo("redirect:" + Mappings.URL_APPLICANT_TYPE);
  }

  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccessWithForm_whenMultiple_thenExcpetion() {
    StepForm testForm = () -> RECEIVE_BENEFITS;

    routeMaster.redirectToOnSuccess(testForm);
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
          public Optional<StepDefinition> determineNextStep() {
            return Optional.of(ELIGIBLE);
          }
        };

    assertThat(routeMaster.redirectToOnSuccess(testForm))
        .isEqualTo("redirect:" + Mappings.URL_ELIGIBLE);
  }

  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccessWithForm_whenMultipleAndFormDeterminesInvalid_thenException() {
    StepForm testForm =
        new StepForm() {
          @Override
          public StepDefinition getAssociatedStep() {
            return RECEIVE_BENEFITS;
          }

          @Override
          public Optional<StepDefinition> determineNextStep() {
            return Optional.of(DECLARATIONS);
          }
        };

    routeMaster.redirectToOnSuccess(testForm);
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
  }

  @Test
  public void isValidState_firstStep() {
    // These steps are always valid;
    Journey journey = new JourneyBuilder().toStep(StepDefinition.HOME).build();
    assertThat(routeMaster.isValidState(StepDefinition.HOME, journey)).isTrue();
    assertThat(routeMaster.isValidState(StepDefinition.APPLICANT_TYPE, journey)).isTrue();
  }
}
