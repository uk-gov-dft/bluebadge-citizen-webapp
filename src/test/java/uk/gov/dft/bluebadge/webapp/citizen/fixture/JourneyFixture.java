package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;

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

    DateOfBirthForm dateOfBirthForm = DateOfBirthForm.builder().day(1).month(1).year(1990).build();

    journey.setApplicantForm(applicantForm);
    journey.setHealthConditionsForm(healthConditionsForm);
    journey.setApplicantNameForm(applicantNameForm);
    journey.setDateOfBirthForm(dateOfBirthForm);

    return journey;
  }
}
