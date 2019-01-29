package uk.gov.dft.bluebadge.webapp.citizen.model;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;

import java.util.HashMap;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class LocaleAwareRefDataTest {

  @Test
  public void getDescriptionTest() {
    LocalAuthorityRefData authorityRefData = new LocalAuthorityRefData();
    authorityRefData.setMetaData(new HashMap<>());

    LocaleAwareRefData<LocalAuthorityRefData> testme = new LocaleAwareRefData<>(authorityRefData);

    // Check nulls
    assertThat(testme.getDescription()).isNull();
    assertThat(testme.getShortCode()).isNull();

    authorityRefData.setDescription("English");
    authorityRefData.setShortCode("LA");

    // No welsh description.
    // English locale
    assertThat(testme.getDescription()).isEqualTo("English");
    assertThat(testme.getShortCode()).isEqualTo("LA");

    // Welsh locale, no description
    Locale welsh = new Locale("cy");
    LocaleContextHolder.setLocale(welsh);
    assertThat(testme.getDescription()).isEqualTo("English");

    // Welsh locale, have description
    authorityRefData.setLocalAuthorityMetaData(new LocalAuthorityRefData.LocalAuthorityMetaData());
    authorityRefData.getLocalAuthorityMetaData().get().setWelshDescription("Welsh");
    assertThat(testme.getDescription()).isEqualTo("Welsh");

    // And when back to English, Welsh description ignored
    Locale english = new Locale("en");
    LocaleContextHolder.setLocale(english);
    assertThat(testme.getDescription()).isEqualTo("English");
  }

}
