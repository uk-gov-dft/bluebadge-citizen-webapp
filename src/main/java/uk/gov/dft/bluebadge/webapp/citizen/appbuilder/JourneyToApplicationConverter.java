package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;

public class JourneyToApplicationConverter {

  private JourneyToApplicationConverter() {}

  public static Application convert(Journey journey) {
    ExistingBadgeForm existingBadgeForm = journey.getFormForStep(StepDefinition.EXISTING_BADGE);

    Application app =
        Application.builder()
            .applicationTypeCode(ApplicationTypeCodeField.NEW)
            .localAuthorityCode(journey.getLocalAuthority().getShortCode())
            .paymentTaken(false)
            .existingBadgeNumber(getExistingBadgeNumber(existingBadgeForm))
            .party(PartyConverter.convert(journey))
            .eligibility(EligibilityConverter.convert(journey))
            .build();

    return app;
  }

  static String getExistingBadgeNumber(ExistingBadgeForm existingBadgeForm) {

    if (existingBadgeForm != null
        && existingBadgeForm.getHasExistingBadge() != null
        && existingBadgeForm.getHasExistingBadge()) {
      return existingBadgeForm.getBadgeNumber();
    }
    return null;
  }
}
