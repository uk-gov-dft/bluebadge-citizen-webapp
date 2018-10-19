package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MainReasonFormTest {

  @Test
  public void determineNextStep_whenTermIll_thenContactCouncil() {
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.TERMILL).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.CONTACT_COUNCIL);
  }

  @Test
  public void determineNextStep_whenWalking_thenContactCouncil() {
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.WALKD).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.WALKING_DIFFICULTY);
  }

  @Test
  public void determineNextStep_whenChildorArms_thenContactCouncil() {
    EnumSet.of(
            EligibilityCodeField.CHILDVEHIC,
            EligibilityCodeField.CHILDBULK,
            EligibilityCodeField.ARMS)
        .forEach(
            e -> {
              MainReasonForm form = MainReasonForm.builder().mainReasonOption(e).build();
              assertThat(form.determineNextStep()).isNotEmpty();
              assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
            });
  }

  @Test
  public void determineNextStep_whenNone_thenNotEligible() {
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.NONE).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.NOT_ELIGIBLE);
  }

  @Test
  public void determineNextStep_whenBlind_thenEligible() {
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.BLIND).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
  }
}
