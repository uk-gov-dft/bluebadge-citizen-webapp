package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceDataResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.SingleReferenceDataResponse;

@Slf4j
@Service
public class ReferenceDataApiClient {

  private static final String RETRIEVE_LA_BY_POSTCODE_ENDPOINT =
      "/reference-data/postcode/{postcode}";

  private final RestTemplate restTemplate;

  @Autowired
  public ReferenceDataApiClient(
      @Qualifier("referenceDataRestTemplate") RestTemplate referenceDataRestTemplate) {
    this.restTemplate = referenceDataRestTemplate;
  }

  /**
   * Retrieve badge reference data
   *
   * @return List of reference data items.
   */
  public List<ReferenceData> retrieveReferenceData(final RefDataDomainEnum referenceDataDomain) {
    Assert.notNull(referenceDataDomain, "retrieveReferenceData - referenceDataDomain must be set");
    log.debug("Loading reference data for domain [{}].", referenceDataDomain);

    ReferenceDataResponse response =
        restTemplate
            .getForEntity(
                UriComponentsBuilder.newInstance()
                    .path("/")
                    .pathSegment("reference-data", referenceDataDomain.name())
                    .toUriString(),
                ReferenceDataResponse.class)
            .getBody();

    return response.getData();
  }

  /**
   * Retrieve reference data for a la by postcode
   *
   * @param postcode, the postcode that is used to find the LA.
   * @return Reference data of the local authority.
   */
  public ReferenceData retrieveLAByPostcode(final String postcode) {
    Assert.notNull(postcode, "retrieveLAByPostcode - postcode must be set");
    log.debug("Loading reference data for la by postcode: [{}]", postcode);

    SingleReferenceDataResponse response =
        restTemplate
            .getForEntity(
                RETRIEVE_LA_BY_POSTCODE_ENDPOINT, SingleReferenceDataResponse.class, postcode)
            .getBody();

    return response.getData();
  }
}
