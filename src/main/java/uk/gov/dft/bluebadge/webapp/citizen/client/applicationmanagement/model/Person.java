package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

  private String badgeHolderName;
  private String nino;
  private LocalDate dob;
  private String nameAtBirth;
  private GenderCodeField genderCode;
}
