package uk.gov.dft.bluebadge.webapp.citizen.model;

import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;

import java.io.Serializable;

@Data
public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";

  private ApplicantForm applicantForm;
  private HealthConditionsForm healthConditionsForm;

  public Boolean isApplicantYourself() {
    if (applicantForm != null) {
      return applicantForm.getApplicantType().equals(ApplicantType.YOURSELF.toString());
    }
    return null;
  }

  public String applicantContextContent(String messageKey) {
    if (isApplicantYourself()) {
      return messageKey;
    }

    return "someone." + messageKey;
  }
}
