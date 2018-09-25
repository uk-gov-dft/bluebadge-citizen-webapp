package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class DateOfBirthForm implements StepForm, Serializable {

  private String day;
  private String month;
  private String year;

  /**
   * Validates the dob is a date in the past.
   *
   * @return true if the date is in the past
   */
  @AssertTrue(message = "{Past.dateOfBirth}")
  public boolean isPastDate() {

    // Cannot evaluate the date
    if (isDatePartMissing()) {
      return true; // picked up by other validation
    }

    // Attempt to parse and compare
    try {
      return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day))
          .isBefore(LocalDate.now());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Builds a LocalDate from the individual date components.
   *
   * @return LocalDate representation of the date
   */
  public LocalDate getLocalDateDob() {
    if (isDatePartMissing()) {
      return null;
    }

    try {
      return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    } catch (Exception e) {
      return null;
    }
  }

  /** Validates the component parts for valid numbers. */
  @AssertFalse(message = "{Invalid.dateofBirth}")
  public boolean isDatePartMissing() {
    return !isParsablePositive(year) || !isParsablePositive(month) || !isParsablePositive(day);
  }

  private boolean isParsablePositive(String number) {
    try {
      Integer n = Integer.parseInt(number);
      return n > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.DOB;
  }
}
