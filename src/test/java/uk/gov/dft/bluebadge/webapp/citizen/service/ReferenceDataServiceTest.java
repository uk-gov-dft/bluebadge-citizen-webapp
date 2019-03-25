package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataDomainEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class ReferenceDataServiceTest {
  private ReferenceDataService referenceDataService;
  @Mock private ReferenceDataApiClient mockApiClient;

  private ObjectMapper objectMapper = new ObjectMapper();
  private List<ReferenceData> refDataList;

  @Before
  @SneakyThrows
  public void setup() {
    initMocks(this);
    objectMapper.registerModule(new Jdk8Module());

    referenceDataService = new ReferenceDataService(mockApiClient);

    Path path =
        Paths.get(getClass().getClassLoader().getResource("mockResponses/refData.json").toURI());
    String json = new String(Files.readAllBytes(path));
    refDataList = objectMapper.readValue(json, new TypeReference<List<ReferenceData>>() {});

    when(mockApiClient.retrieveReferenceData(RefDataDomainEnum.CITIZEN)).thenReturn(refDataList);
  }

  @Test
  public void apiCalledWithConfiguredDomain() {
    referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.NATION);
    verify(mockApiClient).retrieveReferenceData(RefDataDomainEnum.CITIZEN);
  }

  @Test
  public void onceLoaded_thenDoesNotCallAPI() {
    referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.NATION);
    referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.NATION);
    verify(mockApiClient, times(1)).retrieveReferenceData(RefDataDomainEnum.CITIZEN);
  }

  @Test
  public void whenExceptionFromAPI_thenLoadedSetBackToFalse() {
    when(mockApiClient.retrieveReferenceData(RefDataDomainEnum.CITIZEN))
        .thenThrow(new IllegalArgumentException("testing"));

    try {
      referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.NATION);
      fail("No exception thrown");
    } catch (IllegalArgumentException e) {
    }

    try {
      referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.NATION);
      fail("No exception thrown");
    } catch (IllegalArgumentException e) {
    }

    verify(mockApiClient, times(2)).retrieveReferenceData(RefDataDomainEnum.CITIZEN);
  }

  @Test
  public void retrieveLocalCouncil_ShouldReturnLocalAuthorities() {
    List<ReferenceData> referenceDataList =
        referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    assertThat(referenceDataList)
        .extracting("groupShortCode")
        .containsOnly(RefDataGroupEnum.COUNCIL.getGroupKey());
    assertThat(referenceDataList).extracting("shortCode").contains("TON");
  }

  @Test
  public void getLAforLC() {
    // Valid result
    LocalAuthorityRefData localAuthorityRefData =
        referenceDataService.lookupLocalAuthorityFromCouncilCode("TON");
    assertThat(localAuthorityRefData).hasFieldOrPropertyWithValue("shortCode", "WORCC");

    // No match
    localAuthorityRefData = referenceDataService.lookupLocalAuthorityFromCouncilCode("ZZZZZ");
    assertThat(localAuthorityRefData).isNull();

    // Null safe
    localAuthorityRefData = referenceDataService.lookupLocalAuthorityFromCouncilCode(null);
    assertThat(localAuthorityRefData).isNull();

    // And if lc hos no la
    localAuthorityRefData = referenceDataService.lookupLocalAuthorityFromCouncilCode("SPE");
    assertThat(localAuthorityRefData).isNull();
  }

  @Test
  public void retrieveLAByPostcode_ShouldReturnLocalAuthority() {
    ReferenceData la = new LocalAuthorityRefData();
    la.setShortCode("ABERD");
    la.setDescription("Aberdeenshire");
    when(mockApiClient.retrieveLAByPostcode("AA11AA")).thenReturn(la);
    assertThat(referenceDataService.retrieveLAByPostcode("AA11AA")).isEqualTo(la);
  }

  @Test(expected = IllegalArgumentException.class)
  public void retrieveLAByPostcode_shouldThrowIllegalArgumentException_whenPostcodeIsNull() {
    referenceDataService.retrieveLAByPostcode(null);
  }
}
