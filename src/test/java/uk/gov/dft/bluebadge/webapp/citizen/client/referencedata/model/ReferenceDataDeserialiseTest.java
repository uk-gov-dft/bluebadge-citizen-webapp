package uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

public class ReferenceDataDeserialiseTest {

  private ObjectMapper om;
  private String json;

  @Before
  @SneakyThrows
  public void setup() {
    om = new ObjectMapper();
    om.registerModule(new Jdk8Module());
    Path path =
        Paths.get(getClass().getClassLoader().getResource("mockResponses/refData.json").toURI());
    json = new String(Files.readAllBytes(path));
  }

  @Test
  @SneakyThrows
  public void genericRefData() {
    ReferenceData[] referenceData = om.readValue(json, ReferenceData[].class);
    assertThat(referenceData).isNotEmpty();
    assertThat(referenceData).extracting("groupShortCode").doesNotContainNull();
    assertThat(referenceData).extracting("shortCode").doesNotContainNull();
    assertThat(referenceData).extracting("description").doesNotContainNull();
    assertThat(referenceData).extracting("groupShortCode").contains("LA", "LC", "STATUS");

    List<ReferenceData> refData =
        Stream.of(referenceData)
            .filter(rd -> "STATUS".equals(rd.getGroupShortCode()))
            .collect(Collectors.toList());

    assertThat(refData).isNotEmpty();
    assertThat(refData).extracting("metaData").containsOnlyNulls();
  }

  @Test
  @SneakyThrows
  public void localAuthorityRefData() {
    ReferenceData[] referenceData = om.readValue(json, ReferenceData[].class);
    List<LocalAuthorityRefData> localAuths =
        Stream.of(referenceData)
            .filter(rd -> "LA".equals(rd.getGroupShortCode()))
            .map(rd -> (LocalAuthorityRefData) rd)
            .collect(Collectors.toList());

    assertThat(localAuths).isNotEmpty();
    assertThat(localAuths)
        .extracting("contactUrl")
        .containsOnly(
            null,
            "http://www.worcestershire.gov.uk/info/20397/blue_badge/1378/apply_for_or_renew_a_badge");
  }

  @Test
  @SneakyThrows
  public void localCouncilRefData() {
    ReferenceData[] referenceData = om.readValue(json, ReferenceData[].class);
    List<LocalCouncilRefData> localCouncils =
        Stream.of(referenceData)
            .filter(rd -> "LC".equals(rd.getGroupShortCode()))
            .map(rd -> (LocalCouncilRefData) rd)
            .collect(Collectors.toList());

    assertThat(localCouncils).isNotEmpty();
    assertThat(localCouncils)
        .extracting("issuingAuthorityShortCode")
        .containsOnly("WORCC", "SURCC");
  }
}
