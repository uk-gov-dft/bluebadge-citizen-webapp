package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.JourneyFixture;

public class CompensationSchemeFormTest {

  @Test
  public void determineNextStep_whenCompensationIsYes_thenEligible() {
    CompensationSchemeForm form =
        CompensationSchemeForm.builder().hasReceivedCompensation(true).build();

    Journey journey = new JourneyFixture.JourneyBuilder().setEnglishLocalAuthority().build();

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.AFCS_DISABILITY);
  }

  @Test
  public void determineNextStep_whenDisabilityIsNo_AndNationIsNotWelsh_thenMainReason() {

    CompensationSchemeForm form =
        CompensationSchemeForm.builder().hasReceivedCompensation(false).build();

    Journey journey = new JourneyFixture.JourneyBuilder().setEnglishLocalAuthority().build();

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get()).isEqualTo(StepDefinition.MAIN_REASON);
  }

  @Test
  public void determineNextStep_whenDisabilityIsNo_AndNationIsWelsh_thenShowMentalDisorderPage() {

    CompensationSchemeForm form =
        CompensationSchemeForm.builder().hasReceivedCompensation(false).build();

    Journey journey = new JourneyFixture.JourneyBuilder().setWelshLocalAuthority().build();

    assertThat(form.determineNextStep(journey)).isNotEmpty();
    assertThat(form.determineNextStep(journey).get())
        .isEqualTo(StepDefinition.AFCS_MENTAL_DISORDER);
  }
}
