package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

public class JourneyToApplicationConverterTest {

  @Test
  public void convert() {
    Application result =
        JourneyToApplicationConverter.convert(new JourneyBuilder().withEligibility(PIP).build());
    assertThat(result.getLocalAuthorityCode()).isEqualTo(JourneyFixture.Values.LA_SHORT_CODE);
    assertThat(result.getExistingBadgeNumber()).isEqualTo(JourneyFixture.Values.EXISTING_BADGE_NO);
  }

  @Test
  public void getExistingBadgeNumber() {

    assertThat(
            JourneyToApplicationConverter.getExistingBadgeNumber(
                ExistingBadgeForm.builder().hasExistingBadge(false).build()))
        .isNull();
    assertThat(
            JourneyToApplicationConverter.getExistingBadgeNumber(
                ExistingBadgeForm.builder().hasExistingBadge(true).badgeNumber("1").build()))
        .isEqualTo("1");
  }
}
