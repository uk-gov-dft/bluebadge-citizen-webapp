package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

public class JourneyToApplicationConverter {

  private JourneyToApplicationConverter() {}

  public static Application convert(Journey journey) {
    ExistingBadgeForm existingBadgeForm = journey.getFormForStep(StepDefinition.EXISTING_BADGE);

    boolean paymentTaken = journey.isPaymentSuccessful();
    String paymentReference = journey.getPaymentReference();

    return Application.builder()
        .applicationTypeCode(getApplicationType(existingBadgeForm))
        .localAuthorityCode(journey.getLocalAuthority().getShortCode())
        .paymentTaken(paymentTaken)
        .paymentReference(paymentReference)
        .existingBadgeNumber(getExistingBadgeNumber(existingBadgeForm))
        .party(PartyConverter.convert(journey))
        .eligibility(EligibilityConverter.convert(journey))
        .artifacts(ArtifactConverter.convert(journey))
        .build();
  }

  static String getExistingBadgeNumber(ExistingBadgeForm existingBadgeForm) {

    if (existingBadgeForm != null
        && existingBadgeForm.getHasExistingBadge() != null
        && existingBadgeForm.getHasExistingBadge()
        && StringUtils.isNotBlank(existingBadgeForm.getBadgeNumber())) {

      return existingBadgeForm.getBadgeNumber().replaceAll("\\s+", "").substring(0, 6);
    }

    return null;
  }

  static ApplicationTypeCodeField getApplicationType(ExistingBadgeForm existingBadgeForm) {

    if (existingBadgeForm != null
        && existingBadgeForm.getHasExistingBadge() != null
        && existingBadgeForm.getHasExistingBadge()) {
      return ApplicationTypeCodeField.RENEW;
    }

    return ApplicationTypeCodeField.NEW;
  }
}
