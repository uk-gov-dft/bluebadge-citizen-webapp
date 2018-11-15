package uk.gov.dft.bluebadge.webapp.citizen.model.form;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
@GroupSequenceProvider(MedicalEquipmentForm.GroupProvider.class)
public class MedicalEquipmentForm implements StepForm, Serializable {

  @NotEmpty private List<BulkyMedicalEquipmentTypeCodeField> equipment;

  @NotBlank(groups = OtherGroup.class)
  @Size(max = 100, groups = OtherGroup.class)
  private String otherDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MEDICAL_EQUIPMENT;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  public static class GroupProvider implements DefaultGroupSequenceProvider<MedicalEquipmentForm> {
    @Override
    public List<Class<?>> getValidationGroups(MedicalEquipmentForm form) {
      List<Class<?>> sequence = new ArrayList<>();
      sequence.add(MedicalEquipmentForm.class);
      if (null != form
          && null != form.getEquipment()
          && form.getEquipment().contains(BulkyMedicalEquipmentTypeCodeField.OTHER)) {
        sequence.add(OtherGroup.class);
      }
      return sequence;
    }
  }

  interface OtherGroup {}
}
