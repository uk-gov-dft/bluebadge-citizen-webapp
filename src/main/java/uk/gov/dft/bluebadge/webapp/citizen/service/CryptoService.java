package uk.gov.dft.bluebadge.webapp.citizen.service;

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

  private static String VERSION_CONTEXT_KEY = "version";
  private static String POSTCODE_CONTEXT_KEY = "postcode";

  private CryptoApiClient apiClient;
  private BuildProperties buildProperties;

  @Autowired
  CryptoService(CryptoApiClient apiClient, BuildProperties buildProperties) {
    this.apiClient = apiClient;
    this.buildProperties = buildProperties;
  }

  String formatPostcode(String postcode) {
    return StringUtils.trimAllWhitespace(postcode.toLowerCase());
  }

  public String encryptJourney(Journey source, String postcode) {
    log.info("Encrypting journey at version {}", buildProperties.getVersion());

    return apiClient.encrypt(
        Objects.requireNonNull(
            Base64.getEncoder().encodeToString(SerializationUtils.serialize(source))),
        ImmutableMap.of(
            VERSION_CONTEXT_KEY,
            buildProperties.getVersion(),
            POSTCODE_CONTEXT_KEY,
            formatPostcode(postcode)));
  }

  public void checkEncryptedJourneyVersion(String cipherText) throws CryptoVersionException {
    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    log.info(
        "Comparing serialised journey version; App version: {}, Stored version: {}",
        buildProperties.getVersion(),
        encryptedVersion);
    if (!buildProperties.getVersion().equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(
          encryptedVersion, encryptedVersion, buildProperties.getVersion());
    }
  }

  public Journey decryptJourney(String cipherText, String postcode)
      throws CryptoVersionException, CryptoPostcodeException {
    Assert.notNull(postcode, "Postcode required.  Overload method if check not required.");

    DecryptionData decryptionData = apiClient.decrypt(cipherText);
    String encryptedVersion = decryptionData.getEncryptionContext().get(VERSION_CONTEXT_KEY);
    String encryptedPostcode = decryptionData.getEncryptionContext().get(POSTCODE_CONTEXT_KEY);
    if (!buildProperties.getVersion().equalsIgnoreCase(encryptedVersion)) {
      throw new CryptoVersionException(
          encryptedVersion, encryptedVersion, buildProperties.getVersion());
    }
    if (!formatPostcode(postcode).equals(encryptedPostcode)) {
      throw new CryptoPostcodeException(
          "Postcodes don't match", encryptedPostcode, formatPostcode(postcode));
    }

    return SerializationUtils.deserialize(
        Base64.getDecoder().decode(decryptionData.getDecryptResult().getBytes()));
  }
}
