package uk.gov.dft.bluebadge.webapp.citizen.model;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;

@Data
public class RadioOptionsGroup {
  private String titleKey;
  private String hintKey;
  private List<RadioOption> options;

  public RadioOptionsGroup(String title) {
    this.titleKey = title;
  }

  public RadioOptionsGroup(String title, List<RadioOption> options) {
    this.titleKey = title;
    this.options = options;
  }

  public RadioOptionsGroup autoPopulateBooleanOptions() {
    RadioOption yes = new RadioOption("yes", "radio.option.yes");
    RadioOption no = new RadioOption("no", "radio.option.no");
    options = Lists.newArrayList(yes, no);
    return this;
  }
}
