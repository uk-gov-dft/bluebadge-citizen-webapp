package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.time.LocalDate;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateOfBirthForm {

  private Integer day;
  private Integer month;
  private Integer year;

  /**
   * Validates the dob is a date in the past.
   *
   * @return true if the date is in the past
   */
  @AssertTrue(message = "{Past.dateOfBirth}")
  public boolean isPastDate() {

    // Cannot evaluate the date
    if (datePartMissing()) {
      return false;
    }

    // Attempt to parse and compare
    try {
      return LocalDate.of(year, month, day).isBefore(LocalDate.now());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Builds a LocalDate from the individual date components.
   *
   * @return
   */
  public LocalDate getLocalDateDob() {
    if (datePartMissing()) {
      return null;
    }

    try {
      return LocalDate.of(year, month, day);
    } catch (Exception e) {
      return null;
    }
  }

  private boolean datePartMissing() {
    return null == year || null == month || null == day;
  }
}
