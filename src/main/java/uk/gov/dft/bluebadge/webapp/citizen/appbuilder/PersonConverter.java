package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.DOB;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.GENDER;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.NAME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.NINO;

public class PersonConverter {

  public static Person convert(Journey journey) {
    ApplicantNameForm applicantNameForm = journey.getFormForStep(NAME);
    GenderForm genderForm = journey.getFormForStep(GENDER);
    NinoForm ninoForm = journey.getFormForStep(NINO);
    DateOfBirthForm birthForm = journey.getFormForStep(DOB);

    String nino = null == ninoForm ? null : ninoForm.getNino();

    return Person.builder()
        .badgeHolderName(applicantNameForm.getFullName())
        .nameAtBirth(applicantNameForm.getBirthName())
        .dob(birthForm.getDateOfBirth().getLocalDate())
        .genderCode(genderForm.getGender())
        .nino(nino)
        .build();
  }
}
