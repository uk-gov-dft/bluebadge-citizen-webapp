package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class PayForTheBadgeFormTest {

  @Test
  public void determineNextStep_shouldReturnSubmitted_whenPayLater() {
    PayForTheBadgeForm form = PayForTheBadgeForm.builder().build();
    Journey journey = new JourneyBuilder().anAdult().build();

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.SUBMITTED);
  }
}
