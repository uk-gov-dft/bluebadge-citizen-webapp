package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata;

@SuppressWarnings("SpellCheckingInspection")
public enum RefDataGroupEnum {
  COUNCIL("LC"),
  NATION("NATION");

  public String getGroupKey() {
    return groupKey;
  }

  private final String groupKey;

  RefDataGroupEnum(String groupKey) {

    this.groupKey = groupKey;
  }
}
