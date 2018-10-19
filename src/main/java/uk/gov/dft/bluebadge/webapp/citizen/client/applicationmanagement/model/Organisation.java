package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Organisation {
  private String badgeHolderName;
  private Boolean isCharity;
  private String charityNumber;
  private List<Vehicle> vehicles;
  private Integer numberOfBadges;
}
