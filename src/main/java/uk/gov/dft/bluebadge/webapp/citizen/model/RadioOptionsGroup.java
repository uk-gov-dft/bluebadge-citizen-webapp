package uk.gov.dft.bluebadge.webapp.citizen.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.util.Assert;

@Data
public class RadioOptionsGroup {
  private String titleKey;
  private String hintKey;
  // IF true, then title will render as H1, else label
  private boolean titleIsH1 = true;
  private List<RadioOption> options;

  public RadioOptionsGroup(String title) {
    this.titleKey = title;
  }

  public RadioOptionsGroup(String title, List<RadioOption> options) {
    this(title);
    this.options = options;
  }

  public RadioOptionsGroup(String titleKey, boolean titleIsH1, List<RadioOption> options) {
    this(titleKey, options);
    this.titleIsH1 = titleIsH1;
  }

  public RadioOptionsGroup withYesNoOptions() {
    return withYesNoOptions(YesNoType.DEFAULT);
  }

  public RadioOptionsGroup withYesNoOptions(YesNoType type) {
    Assert.notNull(type, "type should not be null");
    String yesMessageKey = "radio.option.yes.".concat(type.toString());
    String noMessageKey = "radio.option.no.".concat(type.toString());

    RadioOption yes = new RadioOption("yes", yesMessageKey);
    RadioOption no = new RadioOption("no", noMessageKey);

    options = Lists.newArrayList(yes, no);
    return this;
  }

  public static class Builder {
    private String titleKey;
    private boolean titleIsH1 = true;
    private final List<RadioOption> options;

    public Builder() {
      options = new ArrayList<>();
    }

    public Builder titleIsLabel() {
      this.titleIsH1 = false;
      return this;
    }

    public Builder titleMessageKey(String titleKey) {
      this.titleKey = titleKey;
      return this;
    }

    public Builder titleMessageKeyApplicantAware(String titleKey, Journey journey) {
      return titleMessageKey(journey.getWho() + titleKey);
    }

    public Builder addOption(Enum<?> value, String messageKey) {
      return addOption(value.name(), messageKey);
    }

    public Builder addOptionApplicantAware(Enum<?> value, String messageKey, Journey journey) {
      return addOption(value.name(), journey.getWho() + messageKey);
    }

    public Builder addOptionApplicantAware(String value, String messageKey, Journey journey) {
      return addOption(value, journey.getWho() + messageKey);
    }

    public Builder addOptionApplicantAndNationAware(
        Enum<?> value, String messageKey, Journey journey) {
      return addOption(value, journey.getWho() + journey.getNation() + "." + messageKey);
    }

    public Builder addOption(String value, String messageKey) {
      options.add(new RadioOption(value, messageKey));
      return this;
    }

    public Builder withYesNoOptions() {
      return withYesNoOptions(YesNoType.DEFAULT);
    }

    public Builder withYesNoOptions(YesNoType type) {
      Assert.notNull(type, "type should not be null");
      String yesMessageKey = "radio.option.yes.".concat(type.toString());
      String noMessageKey = "radio.option.no.".concat(type.toString());

      options.add(new RadioOption("yes", yesMessageKey));
      options.add(new RadioOption("no", noMessageKey));

      return this;
    }

    public RadioOptionsGroup build() {
      return new RadioOptionsGroup(titleKey, titleIsH1, options);
    }
  }
}
