package uk.gov.dft.bluebadge.webapp.citizen.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.CryptoApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionData;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
public class CryptoService {

  private static String VERSION_CONTEXT_KEY = "version";
  private static String POSTCODE_CONTEXT_KEY = "postcode";

  private CryptoApiClient apiClient;

  @Autowired
  CryptoService(CryptoApiClient apiClient) {
    this.apiClient = apiClient;
  }

  String formatPostcode(String postcode) {
    return StringUtils.trimAllWhitespace(postcode.toLowerCase());
  }

  public String encryptJourney(Journey source, String postcode) {
    // TODO real version
    String version = "1.0.0";

    return apiClient.encrypt(
        Objects.requireNonNull(
            Base64.getEncoder().encodeToString(SerializationUtils.serialize(source))),
        ImmutableMap.of(
            VERSION_CONTEXT_KEY, version, POSTCODE_CONTEXT_KEY, formatPostcode(postcode)));
  }

  public void checkEncryptedJourneyVersion(String cipherText, String appVersion) throws CryptoVersionException {
    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    if (!appVersion.equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(encryptedVersion, encryptedVersion, appVersion);
    }
  }

  public Journey decryptJourney(String cipherText, String appVersion, String postcode)
      throws CryptoVersionException, CryptoPostcodeException {
    Assert.notNull(appVersion, "App version required.");
    Assert.notNull(postcode, "Postcode required.  Overload method if check not required.");

    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    String encryptedPostcode = decryptionData.getEncryptionContext().get(POSTCODE_CONTEXT_KEY);
    if (!appVersion.equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(encryptedVersion, encryptedVersion, appVersion);
    }
    if (!formatPostcode(postcode).equals(encryptedPostcode)) {
      throw new CryptoPostcodeException(
          "Postcodes don't match", encryptedPostcode, formatPostcode(postcode));
    }

    return SerializationUtils.deserialize(
        Base64.getDecoder().decode(decryptionData.getDecryptResult().getBytes()));
  }
}
