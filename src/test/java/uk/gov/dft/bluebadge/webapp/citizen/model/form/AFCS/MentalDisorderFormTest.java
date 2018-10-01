package uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class MentalDisorderFormTest {

  @Test
  public void determineNextStep_whenMentalDisorderIsYes_thenEligible() {
    MentalDisorderForm form = MentalDisorderForm.builder().hasMentalDisorder(true).build();

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenMentalDisorderIsNo_thenMainReason() {

    MentalDisorderForm form = MentalDisorderForm.builder().hasMentalDisorder(false).build();

    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.MAIN_REASON);
  }
}
