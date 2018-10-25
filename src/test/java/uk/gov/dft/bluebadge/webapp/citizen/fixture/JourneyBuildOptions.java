package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import lombok.Builder;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;

@Builder
@Getter
public class JourneyBuildOptions {
  private Nation nation;
  private EligibilityCodeField eligibility;
  private StepDefinition step;
  private CompoundDate dob;
  private ApplicantType applicantType;
  private Boolean orgDoesCare;
}
