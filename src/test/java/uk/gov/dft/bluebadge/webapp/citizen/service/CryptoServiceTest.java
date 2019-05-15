package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService.POSTCODE_CONTEXT_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService.VERSION_CONTEXT_KEY;

import com.google.common.collect.ImmutableMap;
import java.util.Base64;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.CryptoApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionData;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class CryptoServiceTest {

  private CryptoService service;
  private BuildProperties buildProperties;
  @Mock private CryptoApiClient mockApiClient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Properties properties = new Properties();
    properties.setProperty("version", "v0.12.1");
    buildProperties = new BuildProperties(properties);
    service = new CryptoService(mockApiClient, buildProperties);
  }

  @Test
  public void extractBuildNumber() {
    // Valid format
    assertThat(service.extractBuildNumber("v1.2.3.somestuff")).isEqualTo("1.2.3");
    // Invalid format
    assertThat(service.extractBuildNumber("1.2.3")).isNull();
    assertThat(service.extractBuildNumber("v1.2.stuff")).isNull();
  }

  @Test
  public void formatPostcode() {
    assertThat(service.formatPostcode(" WV 16 4 aw ")).isEqualTo("wv164aw");
  }

  @Test
  public void encryptJourney() {
    service.encryptJourney(new Journey(), "wv164aw");
    verify(mockApiClient, times(1))
        .encrypt(
            anyString(),
            eq(ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.1", POSTCODE_CONTEXT_KEY, "wv164aw")));
  }

  @Test
  public void checkEncryptedJourneyVersion() {
    when(mockApiClient.decrypt(anyString()))
        .thenReturn(
            DecryptionData.builder()
                .encryptionContext(ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.1"))
                .build());

    try {
      service.checkEncryptedJourneyVersion("abc");
    } catch (CryptoVersionException e) {
      fail("Version should have matched");
    }

    when(mockApiClient.decrypt(anyString()))
        .thenReturn(
            DecryptionData.builder()
                .encryptionContext(ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.2"))
                .build());

    try {
      service.checkEncryptedJourneyVersion("abc");
      fail("Version should not have matched");
    } catch (CryptoVersionException e) {
      // no op
    }
  }

  @Test
  public void decryptJourney() {
    when(mockApiClient.decrypt(anyString()))
        .thenReturn(
            DecryptionData.builder()
                .decryptResult(
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(new Journey())))
                .encryptionContext(
                    ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.1", POSTCODE_CONTEXT_KEY, "wv165aw"))
                .build());

    try {
      service.decryptJourney("stuff", "wv165aw");
    } catch (CryptoVersionException | CryptoPostcodeException e) {
      fail("should have been ok.");
    }
  }

  @Test
  public void decryptJourney_versionWrong() {
    when(mockApiClient.decrypt(anyString()))
        .thenReturn(
            DecryptionData.builder()
                .decryptResult(
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(new Journey())))
                .encryptionContext(
                    ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.2", POSTCODE_CONTEXT_KEY, "wv165aw"))
                .build());

    try {
      service.decryptJourney("stuff", "wv165aw");
      fail("Versions did not match");
    } catch (CryptoPostcodeException e) {
      fail("should have been ok.");
    } catch (CryptoVersionException e) {
      // No op
    }
  }

  @Test
  public void decryptJourney_postcodeWrong() {
    when(mockApiClient.decrypt(anyString()))
        .thenReturn(
            DecryptionData.builder()
                .decryptResult(
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(new Journey())))
                .encryptionContext(
                    ImmutableMap.of(VERSION_CONTEXT_KEY, "0.12.1", POSTCODE_CONTEXT_KEY, "wv167aw"))
                .build());

    try {
      service.decryptJourney("stuff", "wv165aw");
      fail("Postcodes should have not matched.");
    } catch (CryptoVersionException e) {
      fail("should have been ok.");
    } catch (CryptoPostcodeException e) {
      // No op
    }
  }
}
