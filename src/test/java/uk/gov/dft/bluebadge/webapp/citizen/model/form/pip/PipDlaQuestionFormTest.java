package uk.gov.dft.bluebadge.webapp.citizen.model.form.pip;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

public class PipDlaQuestionFormTest {

  @Test
  public void determineNextStep_whenYes_thenEligible() {
    PipDlaQuestionForm form =
        PipDlaQuestionForm.builder()
            .receivedDlaOption(PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA)
            .build();
    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenNo_thenMainReason() {
    PipDlaQuestionForm form =
        PipDlaQuestionForm.builder()
            .receivedDlaOption(PipDlaQuestionForm.PipReceivedDlaOption.NEVER_RECEIVED_DLA)
            .build();
    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.MAIN_REASON);
  }
}
