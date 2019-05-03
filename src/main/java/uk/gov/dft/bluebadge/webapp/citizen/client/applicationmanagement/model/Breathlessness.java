package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Breathlessness {
  private final List<BreathlessnessCodeField> typeCodes;
  private final String otherDescription;

  Breathlessness(List<BreathlessnessCodeField> typeCodes, String otherDescription) {
    this.typeCodes = typeCodes;
    if (null != typeCodes && typeCodes.contains(BreathlessnessCodeField.OTHER)) {
      this.otherDescription = otherDescription;
    } else {
      this.otherDescription = null;
    }
  }
}
