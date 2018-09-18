package uk.gov.dft.bluebadge.webapp.citizen.service.referencedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataDomainEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;

@Service
public class ReferenceDataService {

  private Map<String, List<ReferenceData>> groupedReferenceDataList = null;
  private Map<String, Map<String, String>> groupedReferenceDataMap = null;

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

      groupedReferenceDataMap = new HashMap<>();

      groupedReferenceDataList.forEach(
          (key, value) ->
              groupedReferenceDataMap.put(
                  key,
                  value
                      .stream()
                      .collect(
                          Collectors.toMap(
                              ReferenceData::getShortCode, ReferenceData::getDescription))));
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
}
