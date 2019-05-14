package uk.gov.dft.bluebadge.webapp.citizen.client.crypto;

import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionData;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.DecryptionResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.EncryptionRequest;
import uk.gov.dft.bluebadge.webapp.citizen.client.crypto.model.EncryptionResponse;

@Slf4j
@Service
public class CryptoApiClient {

  private final RestTemplate restTemplate;

  @Autowired
  public CryptoApiClient(
      @Qualifier("cryptoServiceRestTemplate") RestTemplate paymentServiceRestTemplate) {
    this.restTemplate = paymentServiceRestTemplate;
  }

  public String encrypt(String data, Map<String, String> context) {
    Assert.notNull(data, "Data to encrypt must not be null");
    EncryptionRequest request = EncryptionRequest.builder().context(context).data(data).build();

    ResponseEntity<EncryptionResponse> response =
        restTemplate.postForEntity(
            UriComponentsBuilder.newInstance().path("/encryptions").toUriString(),
            request,
            EncryptionResponse.class);

    Assert.isTrue(
        response.getStatusCode().equals(HttpStatus.OK),
        "Unexpected http status from encrypt; " + response.getStatusCode());
    return Objects.requireNonNull(response.getBody()).getData();
  }

  public DecryptionData decrypt(String cipherText) {
    Assert.notNull(cipherText, "Text to encrypt cannot be null.");
    DecryptionRequest request = DecryptionRequest.builder().data(cipherText).build();
    ResponseEntity<DecryptionResponse> response =
        restTemplate.postForEntity(
            UriComponentsBuilder.newInstance().path("/decryptions").toUriString(),
            request,
            DecryptionResponse.class);

    Assert.isTrue(
        response.getStatusCode().equals(HttpStatus.OK),
        "Unexpected http status from decrypt; " + response.getStatusCode());

    return Objects.requireNonNull(response.getBody()).getData();
  }
}
