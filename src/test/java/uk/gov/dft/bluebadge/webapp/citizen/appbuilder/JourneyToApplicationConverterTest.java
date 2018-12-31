package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
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

    assertThat(result.getArtifacts()).hasSize(3);
    assertThat(result.getArtifacts())
        .extracting("type", "link")
        .containsOnly(
            tuple(ArtifactType.PROOF_ELIG, "http://s3/benefitLink"),
            tuple(ArtifactType.PROOF_ID, "http://s3/proveIdLink"),
            tuple(ArtifactType.PHOTO, "http://s3/photoLink"));
  }

  @Test
  public void getExistingBadgeNumber() {

    assertThat(
            JourneyToApplicationConverter.getExistingBadgeNumber(
                ExistingBadgeForm.builder().hasExistingBadge(false).build()))
        .isNull();
    assertThat(
            JourneyToApplicationConverter.getExistingBadgeNumber(
                ExistingBadgeForm.builder().hasExistingBadge(true).badgeNumber("12 34 AB").build()))
        .isEqualTo("1234AB");
  }
}
