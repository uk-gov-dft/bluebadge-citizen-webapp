package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

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
