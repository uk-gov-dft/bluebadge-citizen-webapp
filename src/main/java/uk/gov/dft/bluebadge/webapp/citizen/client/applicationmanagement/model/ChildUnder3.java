package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChildUnder3 {

  private List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes;
  private String otherMedicalEquipment;
}
