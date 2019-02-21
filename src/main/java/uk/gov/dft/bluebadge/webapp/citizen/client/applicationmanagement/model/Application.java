package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Application {

  private String applicationId;
  private ApplicationTypeCodeField applicationTypeCode;
  private String localAuthorityCode;
  private Boolean paymentTaken;
  private String paymentReference;
  private OffsetDateTime submissionDate;
  private String existingBadgeNumber;
  private Party party;
  private Eligibility eligibility;
  private List<Artifact> artifacts;
}
