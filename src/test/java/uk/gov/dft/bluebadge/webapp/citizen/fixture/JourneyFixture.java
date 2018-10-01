package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

public class JourneyFixture {

  public static Journey getDefaultJourney() {
    Journey journey = new Journey();

    HealthConditionsForm healthConditionsForm =
        HealthConditionsForm.builder().descriptionOfConditions("test description").build();

    ApplicantForm applicantForm =
        ApplicantForm.builder().applicantType("What goes in here!").build();

    ApplicantNameForm applicantNameForm =
        ApplicantNameForm.builder()
            .fullName("John Doe")
            .hasBirthName(true)
            .birthName("Johns Birth name")
            .build();

    DateOfBirthForm dateOfBirthForm =
        DateOfBirthForm.builder().dateOfBirth(new CompoundDate("1", "1", "1990")).build();
    //dateOfBirthForm.setDateOfBirth(new CompoundDate());

    ContactDetailsForm contactDetailsForm =
        ContactDetailsForm.builder().primaryPhoneNumber("01270646362").build();

    journey.setApplicantForm(applicantForm);
    journey.setHealthConditionsForm(healthConditionsForm);
    journey.setApplicantNameForm(applicantNameForm);
    journey.setDateOfBirthForm(dateOfBirthForm);
    journey.setReceiveBenefitsForm(
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.WALKD).build());
    journey.setContactDetailsForm(contactDetailsForm);

    return journey;
  }
}
