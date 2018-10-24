package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.SCO;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.WLS;
import static uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture.getDefaultJourneyToStep;

public class JourneyBuilder {
  private Nation nation;
  private EligibilityCodeField eligibility;
  private StepDefinition step;
  private CompoundDate dob;
  private ApplicantType applicantType;

  public JourneyBuilder() {
    // Set defaults
    nation = Nation.ENG;
    eligibility = WALKD;
    step = StepDefinition.DECLARATIONS;
    dob = JourneyFixture.Values.DOB_ADULT;
    applicantType = JourneyFixture.Values.APPLICANT_TYPE;
  }

  public JourneyBuilder forYou() {
    applicantType = ApplicantType.YOURSELF;
    return this;
  }

  public JourneyBuilder forSomeOneElse() {
    applicantType = ApplicantType.SOMEONE_ELSE;
    return this;
  }

  public JourneyBuilder inEngland() {
    nation = Nation.ENG;
    return this;
  }

  public JourneyBuilder inScotland() {
    nation = SCO;
    return this;
  }

  public JourneyBuilder inWales() {
    nation = WLS;
    return this;
  }

  public JourneyBuilder anAdult() {
    dob = JourneyFixture.Values.DOB_ADULT;
    return this;
  }

  public JourneyBuilder aChild() {
    dob = JourneyFixture.Values.DOB_CHILD;
    return this;
  }

  public JourneyBuilder toStep(StepDefinition step){
    this.step = step;
    return this;
  }

  public JourneyBuilder withEligibility(EligibilityCodeField eligibility){
    this.eligibility = eligibility;
    return this;
  }

  public Journey build() {
    return getDefaultJourneyToStep(step, eligibility, nation, dob, applicantType);
  }
}
