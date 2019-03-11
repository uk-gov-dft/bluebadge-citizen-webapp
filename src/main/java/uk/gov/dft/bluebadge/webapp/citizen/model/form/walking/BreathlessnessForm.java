package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BreathlessnessCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
@GroupSequenceProvider(BreathlessnessForm.GroupProvider.class)
public class BreathlessnessForm implements StepForm, Serializable {
  @NotEmpty List<BreathlessnessCodeField> breathlessnessTypes;

  @NotBlank(groups = OtherGroup.class, message = "{NotBlank.breathlessnessOtherDescription}")
  @Size(max = 100, groups = OtherGroup.class, message = "{Size.breathlessnessOtherDescription}")
  String breathlessnessOtherDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.BREATHLESSNESS;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  public static class GroupProvider implements DefaultGroupSequenceProvider<BreathlessnessForm> {
    @Override
    public List<Class<?>> getValidationGroups(BreathlessnessForm object) {
      List<Class<?>> sequence = new ArrayList<>();
      sequence.add(BreathlessnessForm.class);
      if (null != object
          && null != object.getBreathlessnessTypes()
          && object.getBreathlessnessTypes().contains(BreathlessnessCodeField.OTHER)) {
        sequence.add(OtherGroup.class);
      }
      return sequence;
    }
  }

  interface OtherGroup {}
}
