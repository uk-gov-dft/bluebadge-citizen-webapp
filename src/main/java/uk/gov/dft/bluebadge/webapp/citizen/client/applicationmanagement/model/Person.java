package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class Person {

  private String badgeHolderName;
  private String nino;
  private LocalDate dob;
  private String nameAtBirth;
  private GenderCodeField genderCode;
}
