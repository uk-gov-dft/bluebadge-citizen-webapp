package uk.gov.dft.bluebadge.webapp.citizen.model;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.WelshSupport;

public class LocaleAwareRefData<T extends WelshSupport> {

  private T refData;

  public LocaleAwareRefData(T refData) {
    Assert.notNull(refData, "Cannot construct LocaleAwareRefData with null ref data");
    this.refData = refData;
  }

  public String getDescription() {
    if ("cy".equals(LocaleContextHolder.getLocale().getLanguage())
        && null != refData && null != refData.getDescriptionWelsh()) {
      return refData.getDescriptionWelsh();
    }
    return null != refData ? refData.getDescription() : null;
  }

  public String getShortCode() {
    return null != refData ? refData.getShortCode() : null;
  }
}
