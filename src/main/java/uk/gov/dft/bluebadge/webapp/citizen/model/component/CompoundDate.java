package uk.gov.dft.bluebadge.webapp.citizen.model.component;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CompoundDate implements Serializable {

  private String day;
  private String month;
  private String year;

  public CompoundDate() {
    super();
  }

  public CompoundDate(String day, String month, String year) {
    this.day = day;
    this.month = month;
    this.year = year;
  }

  public CompoundDate(LocalDate date) {
    day = Integer.toString(date.getDayOfMonth());
    month = Integer.toString(date.getMonth().getValue());
    year = Integer.toString(date.getYear());
  }
  /**
   * Builds a LocalDate from the individual date components.
   *
   * @return LocalDate representation of the date
   */
  public LocalDate getLocalDate() {
    if (isDatePartMissing()) {
      return null;
    }

    try {
      return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    } catch (Exception e) {
      return null;
    }
  }

  public void clearDate() {
    day = "";
    month = "";
    year = "";
  }

  /** Validates the component parts for valid numbers. */
  public boolean isDatePartMissing() {
    return !isParsablePositive(year) || !isParsablePositive(month) || !isParsablePositive(day);
  }

  private boolean isParsablePositive(String number) {
    try {
      return Integer.parseInt(number) > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }
}
