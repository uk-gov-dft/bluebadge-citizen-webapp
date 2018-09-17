package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

public enum StepDefinitionEnum {
  HOME(Mappings.URL_ROOT, Mappings.URL_APPLICANT_TYPE),
  APPLICANT_TYPE(Mappings.URL_APPLICANT_TYPE, Mappings.URL_APPLICANT_NAME),
  NAME(Mappings.URL_APPLICANT_NAME, Mappings.URL_HEALTH_CONDITIONS),
  DECLARATIONS(Mappings.URL_DECLARATIONS, Mappings.URL_APPLICATION_SUBMITTED),
  HEALTH_CONDITIONS(Mappings.URL_HEALTH_CONDITIONS, Mappings.URL_DECLARATIONS),
  SUBMITTED(Mappings.URL_APPLICATION_SUBMITTED, null);

  private String url;
  private String toUrl;

  StepDefinitionEnum(String url, String toUrl) {
    this.url = url;
    this.toUrl = toUrl;
  }

  public String getUrl() {
    return url;
  }

  String getToUrl() {
    return toUrl;
  }
}
