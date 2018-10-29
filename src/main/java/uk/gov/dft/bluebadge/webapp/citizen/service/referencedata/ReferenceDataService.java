package uk.gov.dft.bluebadge.webapp.citizen.service.referencedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataDomainEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalCouncilRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;

@Service
@Slf4j
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReferenceDataService {

  private Map<String, List<ReferenceData>> groupedReferenceDataList = null;
  private Map<String, LocalCouncilRefData> localCouncilMap = new HashMap<>();
  private Map<String, LocalAuthorityRefData> localAuthorityMap = new HashMap<>();

  private final ReferenceDataApiClient referenceDataApiClient;
  private final RefDataDomainEnum refDataDomain;
  private AtomicBoolean isLoaded = new AtomicBoolean();

  @Autowired
  public ReferenceDataService(ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
    this.refDataDomain = RefDataDomainEnum.CITIZEN;
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting.
   */
  private void init() {
    if (!isLoaded.getAndSet(true)) {

      List<ReferenceData> referenceDataList;
      try {
        referenceDataList = referenceDataApiClient.retrieveReferenceData(refDataDomain);
      } catch (Exception e) {
        isLoaded.getAndSet(false);
        throw e;
      }

      groupedReferenceDataList =
          referenceDataList
              .stream()
              .collect(Collectors.groupingBy(ReferenceData::getGroupShortCode));

      for (ReferenceData item : referenceDataList) {
        if (item instanceof LocalCouncilRefData) {
          localCouncilMap.put(item.getShortCode(), (LocalCouncilRefData) item);
        } else if (item instanceof LocalAuthorityRefData) {
          localAuthorityMap.put(item.getShortCode(), (LocalAuthorityRefData) item);
        }
      }
    }
  }

  private void initialise() {
    if (!isLoaded.get()) {
      init();
    }
  }

  public List<ReferenceData> retrieveReferenceDataList(RefDataGroupEnum referenceDataGroup) {
    initialise();
    return groupedReferenceDataList.get(referenceDataGroup.getGroupKey());
  }

  public LocalAuthorityRefData lookupLocalAuthorityFromCouncilCode(String localCouncilShortCode) {
    initialise();
    LocalCouncilRefData council = localCouncilMap.get(localCouncilShortCode);
    if (null == council) {
      log.warn("No council found for {}.", localCouncilShortCode);
      return null;
    }

    LocalCouncilRefData.LocalCouncilMetaData meta = council.getLocalCouncilMetaData().orElse(null);
    if (null != meta) {
      return localAuthorityMap.get(meta.getIssuingAuthorityShortCode());
    }

    return null;
  }

  public LocalAuthorityRefData retrieveLocalAuthority(String localAuthorityShortCode) {
    return localAuthorityMap.get(localAuthorityShortCode);
  }
}
