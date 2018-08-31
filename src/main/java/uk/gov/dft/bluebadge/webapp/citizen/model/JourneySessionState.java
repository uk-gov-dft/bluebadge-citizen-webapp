package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;

@Builder
@Data
public class JourneySessionState implements Serializable {

  private ApplicantForm applicantForm;
}
