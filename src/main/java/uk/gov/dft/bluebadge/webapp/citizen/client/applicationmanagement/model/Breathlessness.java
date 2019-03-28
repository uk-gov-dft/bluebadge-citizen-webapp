package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Breathlessness {
  private List<BreathlessnessCodeField> typeCodes;
  private String otherDescription;
}
