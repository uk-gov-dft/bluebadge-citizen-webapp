package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.TreatmentWhenType;

@Data
@EqualsAndHashCode
public class TreatmentAddForm implements BaseForm, Serializable {
  @Size(max = 100)
  @NotBlank
  private String treatmentDescription;

  @Size(max = 100)
  private String treatmentWhen;

  @NotNull(message = "{NotNull.treatment.fields.treatmentWhenType}")
  private TreatmentWhenType treatmentWhenType;

  private String treatmentPastWhen;

  private String treatmentOngoingFrequency;

  private String treatmentFutureWhen;

  private String treatmentFutureImprove;

  private String id = UUID.randomUUID().toString();

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of(
        "treatmentDescription",
        "treatmentWhenType",
        "treatmentPastWhen",
        "treatmentOngoingFrequency",
        "treatmentFutureWhen",
        "treatmentFutureImprove",
        "treatmentWhen");
  }
}
