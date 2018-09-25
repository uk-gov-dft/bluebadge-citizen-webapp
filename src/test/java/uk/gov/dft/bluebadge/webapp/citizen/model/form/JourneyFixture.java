package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nations;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import java.util.Optional;

public class JourneyFixture {

  class JourneyBuilder{
    Journey journey;

    public JourneyBuilder() {
      journey = new Journey();
    }

    public JourneyBuilder setEnglishLocalAuthority(){
      return setNation(Nations.ENGLAND);
    }

    public JourneyBuilder setScottishLocalAuthority(){
      return setNation(Nations.SCOTLAND);
    }

    public JourneyBuilder setWelshLocalAuthority(){
      return setNation(Nations.WALES);
    }

    private JourneyBuilder setNation(String nation){
      LocalAuthorityRefData.LocalAuthorityMetaData meta = new LocalAuthorityRefData.LocalAuthorityMetaData();
      meta.setNation(nation);
      LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
      localAuthorityRefData.setLocalAuthorityMetaData(Optional.of(meta));
      journey.setLocalAuthority(localAuthorityRefData);
      return this;
    }

    public Journey build(){
      return journey;
    }
  }
}
