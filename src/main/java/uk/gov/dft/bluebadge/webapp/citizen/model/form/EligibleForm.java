package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

public class EligibleForm implements StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ELIGIBLE;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof EligibleForm)) return false;
    final EligibleForm other = (EligibleForm) o;
    if (!other.canEqual((Object) this)) return false;
    return true;
  }

  public int hashCode() {
    int result = 1;
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof EligibleForm;
  }
}
