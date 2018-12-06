package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.OTHER;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.PUMP;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ChildUnder3;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;

public class MedicalEquipmentConverterTest {

  @Test
  public void convert() {

    ChildUnder3 result =
        MedicalEquipmentConverter.convert(new JourneyBuilder().withEligibility(CHILDBULK).build());
    // Detail covered in other tests.
    assertThat(result.getBulkyMedicalEquipmentTypeCodes().size()).isEqualTo(2);
    assertThat(result.getBulkyMedicalEquipmentTypeCodes()).contains(PUMP);
    assertThat(result.getBulkyMedicalEquipmentTypeCodes()).contains(OTHER);
    assertThat(result.getOtherMedicalEquipment()).isEqualTo("another medical equipment");
  }
}
