package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;

@Data
public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";

  private ApplicantForm applicantForm;

  public Boolean isApplicantYourself() {
    if (applicantForm != null) {
      return applicantForm.getApplicantType().equals(ApplicantType.YOURSELF);
    }
    return null;
  }
}
