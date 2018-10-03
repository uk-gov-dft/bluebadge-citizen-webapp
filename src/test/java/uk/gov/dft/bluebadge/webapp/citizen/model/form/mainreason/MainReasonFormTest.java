package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.JourneyFixture;

public class MainReasonFormTest {

  @Test
  public void determineNextStep_whenTermIll_thenContactCouncil() {
    Journey journey = new JourneyFixture.JourneyBuilder().setEnglishLocalAuthority().build();
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.TERMILL).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.CONTACT_COUNCIL);
  }

  @Test
  public void determineNextStep_whenWalking_thenContactCouncil() {
    Journey journey = new JourneyFixture.JourneyBuilder().setEnglishLocalAuthority().build();
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
    Journey journey = new JourneyFixture.JourneyBuilder().setEnglishLocalAuthority().build();
    MainReasonForm form =
        MainReasonForm.builder().mainReasonOption(EligibilityCodeField.BLIND).build();
    assertThat(form.determineNextStep()).isNotEmpty();
    assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
  }
}
