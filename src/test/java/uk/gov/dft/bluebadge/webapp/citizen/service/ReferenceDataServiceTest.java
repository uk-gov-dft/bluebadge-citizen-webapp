package uk.gov.dft.bluebadge.webapp.citizen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.APP_SOURCE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.DELIVERY_OPTIONS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.DELIVER_TO;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.ELIGIBILITY;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.GENDER;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.LA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum.STATUS;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.ReferenceDataApiClient;
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
    referenceDataService.retrieveLocalAuthorities();
    verify(mockApiClient).retrieveReferenceData(RefDataDomainEnum.CITIZEN);
  }

  @Test
  public void retrieveEligibilities_ShouldReturnEligibilities() {
    List<ReferenceData> eligibilities = referenceDataService.retrieveEligilities();
    assertThat(eligibilities).extracting("groupShortCode").containsOnly(ELIGIBILITY.getGroupKey());
    assertThat(eligibilities).extracting("shortCode").containsOnly("PIP");
  }

  @Test
  public void retrieveGenders_ShouldReturnGender() {
    List<ReferenceData> gender = referenceDataService.retrieveGenders();
    assertThat(gender).extracting("groupShortCode").containsOnly(GENDER.getGroupKey());
    assertThat(gender).extracting("shortCode").containsOnly("MALE");
  }

  @Test
  public void retrieveApplicationChannels_ShouldReturnApplicationChannels() {
    List<ReferenceData> applicationChannels = referenceDataService.retrieveApplicationChannels();
    assertThat(applicationChannels)
        .extracting("groupShortCode")
        .containsOnly(APP_SOURCE.getGroupKey());
    assertThat(applicationChannels).extracting("shortCode").containsOnly("INPERSON");
  }

  @Test
  public void retrieveDeliverTos_ShouldReturnDeliverTos() {
    List<ReferenceData> deliverTos = referenceDataService.retrieveDeliverTos();
    assertThat(deliverTos).extracting("groupShortCode").containsOnly(DELIVER_TO.getGroupKey());
    assertThat(deliverTos).extracting("shortCode").containsOnly("HOME");
  }

  @Test
  public void retrieveDeliveryOptions_ShouldReturnDeliveryOptions() {
    List<ReferenceData> deliveryOptions = referenceDataService.retrieveDeliveryOptions();
    assertThat(deliveryOptions)
        .extracting("groupShortCode")
        .containsOnly(DELIVERY_OPTIONS.getGroupKey());
    assertThat(deliveryOptions).extracting("shortCode").containsOnly("FAST");
  }

  @Test
  public void retrieveStatuses_ShouldReturnStatuses() {
    List<ReferenceData> statuses = referenceDataService.retrieveStatuses();
    assertThat(statuses).extracting("groupShortCode").containsOnly(STATUS.getGroupKey());
    assertThat(statuses).extracting("shortCode").containsOnly("RETURNED");
  }

  @Test
  public void retrieveLocalAuthorities_ShouldReturnLocalAuthorities() {
    List<ReferenceData> las = referenceDataService.retrieveLocalAuthorities();
    assertThat(las).extracting("groupShortCode").containsOnly(LA.getGroupKey());
    assertThat(las).extracting("shortCode").contains("WORCC", "YORK");
  }

  @Test
  public void retrieveEligibilityDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveEligibilityDisplayValue("PIP")).isEqualTo("PIP");
  }

  @Test
  public void retrieveGenderDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveGenderDisplayValue("MALE")).isEqualTo("Male");
  }

  @Test
  public void retrieveApplicationChannelDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveApplicationChannelDisplayValue("INPERSON"))
        .isEqualTo("In person");
  }

  @Test
  public void retrieveDeliverToDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveDeliverToDisplayValue("HOME"))
        .isEqualTo("Home address");
  }

  @Test
  public void retrieveDeliveryOptionsDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveDeliveryOptionDisplayValue("FAST"))
        .isEqualTo("Fast track");
  }

  @Test
  public void retrieveStatusDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveStatusDisplayValue("RETURNED")).isEqualTo("Returned");
  }

  @Test
  public void retrieveLocalAuthorityDisplayValue_ShouldWork() {
    assertThat(referenceDataService.retrieveLocalAuthorityDisplayValue("YORK"))
        .isEqualTo("City of York Council");
  }
}
