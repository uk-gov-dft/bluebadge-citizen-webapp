package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

public class JourneyToApplicationConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void convert() {
    JourneyFixture.configureMockJourney(journey, EligibilityCodeField.PIP);
    Application result = JourneyToApplicationConverter.convert(journey);
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
