package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class JourneyToApplicationConverter {

  public static Application convert(Journey journey) {
    return Application.builder()
        .applicationTypeCode(ApplicationTypeCodeField.NEW)
        .localAuthorityCode(journey.getLocalAuthority().getShortCode())
        .paymentTaken(false)
        .existingBadgeNumber(null)
        .party(PartyConverter.convert(journey))
        .eligibility(EligibilityConverter.convert(journey))
        .build();
  }
}
