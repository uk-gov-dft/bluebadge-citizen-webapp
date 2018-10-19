package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import com.google.common.collect.ImmutableList;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class JourneyFixture {

  public static Journey getDefaultJourney() {
    Journey journey = new Journey();

    YourIssuingAuthorityForm yourIssuingAuthorityForm = YourIssuingAuthorityForm.builder().localAuthorityShortCode("ABERD").build();

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

    GenderForm genderForm = GenderForm.builder().gender(GenderCodeField.FEMALE).build();

    EnterAddressForm enterAddressForm =
        EnterAddressForm.builder()
            .buildingAndStreet("High Street 1")
            .optionalAddress("District")
            .townOrCity("London")
            .postcode("BR4 9NA")
            .build();
    journey.setEnterAddressForm(enterAddressForm);

    ContactDetailsForm contactDetailsForm =
        ContactDetailsForm.builder().primaryPhoneNumber("01270646362").build();

    WhereCanYouWalkForm whereCanYouWalkForm =
        WhereCanYouWalkForm.builder()
            .destinationToHome("London")
            .timeToDestination("10 minutes")
            .build();

    journey.setApplicantForm(applicantForm);
    journey.setYourIssuingAuthorityForm(yourIssuingAuthorityForm);
    journey.setHealthConditionsForm(healthConditionsForm);
    journey.setApplicantNameForm(applicantNameForm);
    journey.setDateOfBirthForm(dateOfBirthForm);
    journey.setGenderForm(genderForm);
    journey.setReceiveBenefitsForm(
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.WALKD).build());
    journey.setWhereCanYouWalkForm(whereCanYouWalkForm);
    journey.setContactDetailsForm(contactDetailsForm);

    WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(
                ImmutableList.of(
                    WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.SOMELSE))
            .somethingElseDescription("Some description of walking")
            .build();
    journey.setWhatMakesWalkingDifficultForm(whatMakesWalkingDifficultForm);
    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.LESSMIN).build();
    journey.setWalkingTimeForm(walkingTimeForm);

    return journey;
  }
}
