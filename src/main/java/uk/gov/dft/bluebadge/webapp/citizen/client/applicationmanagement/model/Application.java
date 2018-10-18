package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Builder
@Data
public class Application {

  private String applicationId;
  private ApplicationTypeCodeField applicationTypeCode;
  private String localAuthorityCode;
  private Boolean paymentTaken;
  private OffsetDateTime submissionDate;
  private String existingBadgeNumber;
  private Party party;
  private Eligibility eligibility;
  private Artifacts artifacts;
}
