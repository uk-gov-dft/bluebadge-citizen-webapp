package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.util.List;
import lombok.Data;

@Data
public class RadioOptionsGroup {
  private String titleKey;
  private String hintKey;
  private List<RadioOption> options;

  public RadioOptionsGroup(String title, List<RadioOption> options) {
    this.titleKey = title;
    this.options = options;
  }
}
