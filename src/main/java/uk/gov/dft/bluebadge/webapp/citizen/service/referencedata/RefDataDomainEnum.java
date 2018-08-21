package uk.gov.dft.bluebadge.webapp.citizen.service.referencedata;

public enum RefDataDomainEnum {
  BADGE("BADGE"),
  MESSAGE("MESSAGE"),
  USER("USER"),
  APP("APP");

  public String getDomain() {
    return domain;
  }

  private final String domain;

  RefDataDomainEnum(String domain) {

    this.domain = domain;
  }
}
