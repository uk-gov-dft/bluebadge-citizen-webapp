package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class ChooseYourCouncilFormTest {

  @Mock private Journey journeyMock;

  private ChooseYourCouncilForm form;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    form = ChooseYourCouncilForm.builder().build();
  }

  @Test
  public void
      determineNextStep_whenLocalAuthorityIsActive_thenRedirectToYourIssuingAuthorityPage() {
    when(journeyMock.isLocalAuthorityActive()).thenReturn(true);
    assertThat(form.determineNextStep(journeyMock))
        .isEqualTo(Optional.of(StepDefinition.YOUR_ISSUING_AUTHORITY));
  }

  @Test
  public void
      determineNextStep_whenLocalAuthorityIsNotActive_thenRedirectToDifferentServiceSignpostPage() {
    when(journeyMock.isLocalAuthorityActive()).thenReturn(false);
    assertThat(form.determineNextStep(journeyMock))
        .isEqualTo(Optional.of(StepDefinition.DIFFERENT_SERVICE_SIGNPOST));
  }
}
