package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

class WalkingTimeFormTest {

  @Test
  void getAssociatedStep() {
    WalkingTimeForm walkingTimeForm = WalkingTimeForm.builder().build();
    assertThat(walkingTimeForm.getAssociatedStep()).isEqualTo(StepDefinition.WALKING_TIME);
  }

  @Test
  public void routeMaster_validSteps() {
    RouteMaster routeMaster = RouteMasterFixture.routeMaster();
    Journey journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.WALKING_TIME, EligibilityCodeField.WALKD, false);

    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.CANTWALK).build();
    routeMaster.redirectToOnSuccess(walkingTimeForm, journey);

    EnumSet<WalkingLengthOfTimeCodeField> otherTypes =
        EnumSet.complementOf(EnumSet.of(WalkingLengthOfTimeCodeField.CANTWALK));
    assertThat(otherTypes).isNotEmpty();

    for (WalkingLengthOfTimeCodeField walkingTimeType : otherTypes) {
      walkingTimeForm = WalkingTimeForm.builder().walkingTime(walkingTimeType).build();
      routeMaster.redirectToOnSuccess(walkingTimeForm, journey);
    }
  }
}
