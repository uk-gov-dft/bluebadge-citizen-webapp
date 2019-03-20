package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

class WalkingTimeFormTest {

  @Test
  void getAssociatedStep() {
    WalkingTimeForm walkingTimeForm = WalkingTimeForm.builder().build();
    assertThat(walkingTimeForm.getAssociatedStep()).isEqualTo(StepDefinition.WALKING_TIME);
  }

  @Test
  void determineNextStep_whenCantWalk() {
    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.CANTWALK).build();
    Optional<StepDefinition> actual = walkingTimeForm.determineNextStep(null);
    assertThat(actual.get()).isEqualTo(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
  }

  @Test
  void determineNextStep_whenAnyOther() {
    EnumSet<WalkingLengthOfTimeCodeField> otherTypes =
        EnumSet.complementOf(EnumSet.of(WalkingLengthOfTimeCodeField.CANTWALK));
    assertThat(otherTypes).isNotEmpty();

    for (WalkingLengthOfTimeCodeField walkingTimeType : otherTypes) {
      WalkingTimeForm walkingTimeForm =
          WalkingTimeForm.builder().walkingTime(walkingTimeType).build();
      Optional<StepDefinition> actual = walkingTimeForm.determineNextStep(null);
      assertThat(actual.get()).isEqualTo(StepDefinition.WHERE_CAN_YOU_WALK);
    }
  }

  @Test
  public void routeMaster_validSteps() {
    RouteMaster routeMaster = new RouteMaster();

    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.CANTWALK).build();
    routeMaster.redirectToOnSuccess(walkingTimeForm);

    EnumSet<WalkingLengthOfTimeCodeField> otherTypes =
        EnumSet.complementOf(EnumSet.of(WalkingLengthOfTimeCodeField.CANTWALK));
    assertThat(otherTypes).isNotEmpty();

    for (WalkingLengthOfTimeCodeField walkingTimeType : otherTypes) {
      walkingTimeForm = WalkingTimeForm.builder().walkingTime(walkingTimeType).build();
      routeMaster.redirectToOnSuccess(walkingTimeForm);
    }
  }
}
