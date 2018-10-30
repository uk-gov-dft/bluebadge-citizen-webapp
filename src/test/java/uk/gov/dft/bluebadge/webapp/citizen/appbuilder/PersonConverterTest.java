package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

public class PersonConverterTest {

  @Test
  public void convert() {
    Journey journey = new JourneyBuilder().forYou().build();
    Person result = PersonConverter.convert(journey);
    assertThat(result.getBadgeHolderName()).isEqualTo(JourneyFixture.Values.FULL_NAME);
    assertThat(result.getNameAtBirth()).isEqualTo(JourneyFixture.Values.BIRTH_NAME);
    assertThat(result.getGenderCode()).isEqualTo(JourneyFixture.Values.GENDER);
    assertThat(result.getDob()).isEqualTo(JourneyFixture.Values.DOB_AS_EQUAL_TO_STRING);
    assertThat(result.getNino()).isEqualTo(JourneyFixture.Values.NINO);

    // Try with null nino.
    NinoForm ninoForm = journey.getFormForStep(StepDefinition.NINO);
    ninoForm.setNino(null);
    result = PersonConverter.convert(journey);
    assertThat(result.getNino()).isNull();
  }

  @Test
  public void convert_young_applicant() {
    // Journey for adult with Nino.
    Journey journey = new JourneyBuilder().forYou().anAdult().build();
    Person result = PersonConverter.convert(journey);
    assertThat(result.getNino()).isEqualTo(JourneyFixture.Values.NINO);

    // Then user changes to a young applicant.
    DateOfBirthForm birthForm = journey.getFormForStep(StepDefinition.DOB);
    birthForm.setDateOfBirth(JourneyFixture.Values.DOB_CHILD);
    result = PersonConverter.convert(journey);
    assertThat(result.getNino()).isNull();
  }
}
