package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.*;

@Data
public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";

  private ApplicantForm applicantForm;
  private ApplicantNameForm applicantNameForm;
  private HealthConditionsForm healthConditionsForm;
  private ReceiveBenefitsForm receiveBenefitsForm;
  private HigherRateMobilityForm higherRateMobilityForm;
  private ChooseYourCouncilForm chooseYourCouncilForm;
  private YourIssuingAuthorityForm yourIssuingAuthorityForm;

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

  public boolean isValidState(StepDefinition step) {
    if (null == getApplicantForm()) {
      return false;
    }

    switch (step) {
      case ELIGIBLE:
      case MAY_BE_ELIGIBLE:
        if (null == getYourIssuingAuthorityForm()) {
          return false;
        }
    }

    return true;
  }
}
