package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PersonConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void convert() {
    ApplicantNameForm applicantNameForm =
        ApplicantNameForm.builder()
            .birthName("Birth")
            .fullName("Full")
            .hasBirthName(Boolean.TRUE)
            .build();
    GenderForm genderForm = GenderForm.builder().gender(GenderCodeField.FEMALE).build();
    NinoForm ninoForm = NinoForm.builder().nino("NS123456D").build();
    DateOfBirthForm birthForm =
        DateOfBirthForm.builder().dateOfBirth(new CompoundDate("29", "5", "1970")).build();

    when(journey.getFormForStep(StepDefinition.NAME)).thenReturn(applicantNameForm);
    when(journey.getFormForStep(StepDefinition.GENDER)).thenReturn(genderForm);
    when(journey.getFormForStep(StepDefinition.NINO)).thenReturn(ninoForm);
    when(journey.getFormForStep(StepDefinition.DOB)).thenReturn(birthForm);

    Person result = PersonConverter.convert(journey);
    assertThat(result.getBadgeHolderName()).isEqualTo("Full");
    assertThat(result.getNameAtBirth()).isEqualTo("Birth");
    assertThat(result.getGenderCode()).isEqualTo(GenderCodeField.FEMALE);
    assertThat(result.getDob()).isEqualTo("1970-05-29");
    assertThat(result.getNino()).isEqualTo("NS123456D");

    // Try with null nino.
    when(journey.getFormForStep(StepDefinition.NINO)).thenReturn(null);
    result = PersonConverter.convert(journey);
    assertThat(result.getNino()).isNull();
  }
}
