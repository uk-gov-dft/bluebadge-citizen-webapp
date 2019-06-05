package uk.gov.dft.bluebadge.webapp.citizen.service;

import static uk.gov.dft.bluebadge.webapp.citizen.utilities.CookieUtils.extractBuildNumber;

import com.google.common.collect.ImmutableMap;
import java.util.Base64;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.CryptoApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionData;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@Service
public class CryptoService {

  static final String VERSION_CONTEXT_KEY = "version";
  static final String POSTCODE_CONTEXT_KEY = "postcode";

  private CryptoApiClient apiClient;
  private final String buildNumber;

  @Autowired
  CryptoService(CryptoApiClient apiClient, BuildProperties buildProperties) {
    this.apiClient = apiClient;
    buildNumber = extractBuildNumber(buildProperties.getVersion());
    Assert.isTrue(
        null != buildNumber && buildNumber.length() > 1, "Could not extract build version.");
    log.info("Crypto client created for citizen app build no: {}", buildProperties);
  }

  String formatPostcode(String postcode) {
    return StringUtils.trimAllWhitespace(postcode.toLowerCase());
  }

  /**
   * Encrypt journey with context of postcode and app version.
   *
   * @param source Journey to encrypt.
   * @param postcode Postcode to store in encrypted string as context variable.
   * @return Cipher text
   */
  public String encryptJourney(Journey source, String postcode) {
    log.info("Encrypting journey at version {}", buildNumber);

    return apiClient.encrypt(
        Objects.requireNonNull(
            Base64.getEncoder().encodeToString(SerializationUtils.serialize(source))),
        ImmutableMap.of(
            VERSION_CONTEXT_KEY, buildNumber, POSTCODE_CONTEXT_KEY, formatPostcode(postcode)));
  }

  /**
   * Decrypt string and compare context variable holding version.
   *
   * @param cipherText Encrypted data with context variable stored within.
   * @throws CryptoVersionException If application version does not match context version.
   */
  public void checkEncryptedJourneyVersion(String cipherText) throws CryptoVersionException {
    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    log.info(
        "Comparing serialised journey version; App version: {}, Stored version: {}",
        buildNumber,
        encryptedVersion);
    if (!buildNumber.equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(encryptedVersion, buildNumber);
    }
  }

  /**
   * Decrypt a journey.
   *
   * @param cipherText Encrypted journey.
   * @param postcode Postcode used when encrypted.
   * @return Decrypted, deserialized Journey.
   * @throws CryptoVersionException Journey was encrypted with a different version of the app.
   *     Probably cannot deserialize. If did deserialize then undefined behaviour.
   * @throws CryptoPostcodeException Journey was encrypted with a different postcode. Failed
   *     security check.
   */
  public Journey decryptJourney(String cipherText, String postcode)
      throws CryptoVersionException, CryptoPostcodeException {
    Assert.notNull(postcode, "Postcode required.");

    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    String encryptedPostcode = decryptionData.getEncryptionContext().get(POSTCODE_CONTEXT_KEY);
    if (!buildNumber.equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(encryptedVersion, buildNumber);
    }
    if (!formatPostcode(postcode).equals(encryptedPostcode)) {
      throw new CryptoPostcodeException(encryptedPostcode, formatPostcode(postcode));
    }

    // All good, decrypt.
    return SerializationUtils.deserialize(
        Base64.getDecoder().decode(decryptionData.getDecryptResult().getBytes()));
  }
}
