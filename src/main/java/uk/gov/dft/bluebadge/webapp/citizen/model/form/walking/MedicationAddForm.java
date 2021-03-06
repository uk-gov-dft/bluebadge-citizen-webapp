package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

@Data
@EqualsAndHashCode
public class MedicationAddForm implements BaseForm, Serializable {

  @Size(max = 100, message = "{Size.medication.name}")
  @NotBlank(message = "{NotBlank.medication.name}")
  private String name;

  // yes - no value with radio buttons..
  @NotNull(message = "{NotNull.medication.prescribed}")
  private String prescribed;

  @Size(max = 100, message = "{Size.medication.dosage}")
  @NotBlank(message = "{NotBlank.medication.dosage}")
  private String dosage;

  @Size(max = 100, message = "{Size.medication.frequency}")
  @NotBlank(message = "{NotBlank.medication.frequency}")
  private String frequency;

  private String id;

  public MedicationAddForm() {
    id = UUID.randomUUID().toString();
  }

  public Boolean getPrescribedValue() {
    if ("yes".equals(prescribed)) return true;
    if ("no".equals(prescribed)) return false;
    return null;
  }

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("name", "prescribed", "dosage", "frequency");
  }
}
