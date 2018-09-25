package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class PipPlanningJourneyFormTest extends JourneyFixture {

  EnumSet<PipPlanningJourneyForm.PipPlanningJourneyOption> twelveOrMore =
      EnumSet.of(PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_12);

  @Test
  public void determineNextStep_when12OrMorePoints_thenIsEligible() {
    Journey journey = new JourneyBuilder().setEnglishLocalAuthority().build();
    twelveOrMore.forEach(
        e -> {
          PipPlanningJourneyForm form =
              PipPlanningJourneyForm.builder().planningJourneyOption(e).build();
          assertThat(form.determineNextStep(journey)).isNotEmpty();
          assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.ELIGIBLE);
        });
  }

  @Test
  public void determineNextStep_whenLessThan12PointsWales_thenMayBeEligible() {
    Journey journey = new JourneyBuilder().setWelshLocalAuthority().build();
    EnumSet.complementOf(twelveOrMore)
        .forEach(
            e -> {
              PipPlanningJourneyForm form =
                  PipPlanningJourneyForm.builder().planningJourneyOption(e).build();
              assertThat(form.determineNextStep(journey)).isNotEmpty();
              assertThat(form.determineNextStep(journey).get())
                  .isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
            });
  }

  @Test
  public void determineNextStep_whenLessThan12PointsScotland_thenPipDla() {
    Journey journey = new JourneyBuilder().setScottishLocalAuthority().build();
    EnumSet.complementOf(twelveOrMore)
        .forEach(
            e -> {
              PipPlanningJourneyForm form =
                  PipPlanningJourneyForm.builder().planningJourneyOption(e).build();
              assertThat(form.determineNextStep(journey)).isNotEmpty();
              assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.PIP_DLA);
            });
  }
}
