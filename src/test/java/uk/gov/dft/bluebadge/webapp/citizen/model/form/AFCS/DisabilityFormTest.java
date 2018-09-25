package uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

public class DisabilityFormTest {
    @Test
    public void determineNextStep_whenDisabilityIsTrue_thenEligible() {
        DisabilityForm form =
                DisabilityForm.builder().hasDisability(true).build();

        assertThat(form.determineNextStep()).isNotEmpty();
        assertThat(form.determineNextStep().get()).isEqualTo(StepDefinition.ELIGIBLE);
    }

    @Test
    public void determineNextStep_whenDisabilityIsFalse_AndNationIsNotWales_thenMaybeEligible() {

        String Nation = "Wales";
        DisabilityForm form = DisabilityForm.builder().hasDisability(false).build();

        assertThat(form.determineNextStep()).isNotEmpty();
        assertThat(form.determineNextStep().get())
                .isEqualTo(StepDefinition.MAY_BE_ELIGIBLE);
    }
}
