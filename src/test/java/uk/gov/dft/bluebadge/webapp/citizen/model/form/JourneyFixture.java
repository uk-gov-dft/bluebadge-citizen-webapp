package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.util.Optional;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class JourneyFixture {

  public static class JourneyBuilder {
    Journey journey;

    public JourneyBuilder() {
      journey = new Journey();
    }

    public JourneyBuilder setEnglishLocalAuthority() {
      return setNation(Nation.ENG);
    }

    public JourneyBuilder setScottishLocalAuthority() {
      return setNation(Nation.SCO);
    }

    public JourneyBuilder setWelshLocalAuthority() {
      return setNation(Nation.WLS);
    }

    private JourneyBuilder setNation(Nation nation) {
      LocalAuthorityRefData.LocalAuthorityMetaData meta =
          new LocalAuthorityRefData.LocalAuthorityMetaData();
      meta.setNation(nation);
      LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
      localAuthorityRefData.setLocalAuthorityMetaData(Optional.of(meta));
      journey.setLocalAuthority(localAuthorityRefData);
      return this;
    }

    public Journey build() {
      return journey;
    }
  }
}
