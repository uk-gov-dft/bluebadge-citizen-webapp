package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.util.Optional;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nations;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class JourneyFixture {

  public static class JourneyBuilder {
    Journey journey;

    public JourneyBuilder() {
      journey = new Journey();
    }

    public JourneyBuilder setEnglishLocalAuthority() {
      return setNation(Nations.ENGLAND);
    }

    public JourneyBuilder setScottishLocalAuthority() {
      return setNation(Nations.SCOTLAND);
    }

    public JourneyBuilder setWelshLocalAuthority() {
      return setNation(Nations.WALES);
    }

    public JourneyBuilder setDateOfBirth(String year, String month, String day){
      journey.setDateOfBirthForm(DateOfBirthForm.builder().year(year).month(month).day(day).build());
      return this;
    }

    private JourneyBuilder setNation(String nation) {
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
