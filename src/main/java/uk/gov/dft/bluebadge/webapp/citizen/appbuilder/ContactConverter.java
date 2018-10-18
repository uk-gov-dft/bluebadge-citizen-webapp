package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Contact;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;

public class ContactConverter {

  // TODO make package scoped
  public static Contact convert(Journey journey){
    EnterAddressForm enterAddressForm = journey.getFormForStep(StepDefinition.ADDRESS);
    ContactDetailsForm contactDetailsForm = journey.getFormForStep(StepDefinition.CONTACT_DETAILS);
    
    return Contact.builder()
        .buildingStreet(enterAddressForm.getBuildingAndStreet())
        .line2(enterAddressForm.getOptionalAddress())
        .townCity(enterAddressForm.getTownOrCity())
        .postCode(enterAddressForm.getPostcode())
        .fullName(contactDetailsForm.getFullName())
        .primaryPhoneNumber(contactDetailsForm.getPrimaryPhoneNumber())
        .secondaryPhoneNumber(contactDetailsForm.getSecondaryPhoneNumber())
        .emailAddress(contactDetailsForm.getEmailAddress())
        .build();
  }
}
