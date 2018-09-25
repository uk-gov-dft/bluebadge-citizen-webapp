package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_0;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_4;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class PipMovingAroundFormTest extends JourneyFixture {

  EnumSet<PipMovingAroundForm.PipMovingAroundOption> lessThan8 =
      EnumSet.of(MOVING_POINTS_4, MOVING_POINTS_0);

  @Test
  public void determineNextStep_when8OrMorePoints_thenIsEligible() {
    Journey journey = new JourneyBuilder().setEnglishLocalAuthority().build();
    EnumSet.complementOf(lessThan8)
        .forEach(
            e -> {
              PipMovingAroundForm form =
                  PipMovingAroundForm.builder().movingAroundPoints(e).build();
              assertThat(form.determineNextStep(journey)).isNotEmpty();
              assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.ELIGIBLE);
            });
  }

  @Test
  public void determineNextStep_whenLessThan8PointsAndEngland_thenMayBeEligible() {
    Journey journey = new JourneyBuilder().setEnglishLocalAuthority().build();
    lessThan8.forEach(
        e -> {
          PipMovingAroundForm form = PipMovingAroundForm.builder().movingAroundPoints(e).build();
          assertThat(form.determineNextStep(journey)).isNotEmpty();
          assertThat(form.determineNextStep(journey).get())
              .isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
        });
  }

  @Test
  public void determineNextStep_whenLessThan8PointsAndScotland_thenPipPlanningPage() {
    Journey journey = new JourneyBuilder().setScottishLocalAuthority().build();
    lessThan8.forEach(
        e -> {
          PipMovingAroundForm form = PipMovingAroundForm.builder().movingAroundPoints(e).build();
          assertThat(form.determineNextStep(journey)).isNotEmpty();
          assertThat(form.determineNextStep(journey).get())
              .isEqualTo(StepDefinition.PIP_PLANNING_JOURNEY);
        });
  }

  @Test
  public void determineNextStep_whenLessThan8PointsAndWales_thenPipPlanningPage() {
    Journey journey = new JourneyBuilder().setWelshLocalAuthority().build();
    lessThan8.forEach(
        e -> {
          PipMovingAroundForm form = PipMovingAroundForm.builder().movingAroundPoints(e).build();
          assertThat(form.determineNextStep(journey)).isNotEmpty();
          assertThat(form.determineNextStep(journey).get())
              .isEqualTo(StepDefinition.PIP_PLANNING_JOURNEY);
        });
  }
}
