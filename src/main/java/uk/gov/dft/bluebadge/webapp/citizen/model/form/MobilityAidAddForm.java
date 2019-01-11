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
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;

@Data
@EqualsAndHashCode
public class MobilityAidAddForm implements BaseForm, Serializable {

  @NotBlank
  @Size(max = 100)
  private String aidType;

  @Size(max = 100)
  @NotBlank
  private String usage;

  @NotNull private HowProvidedCodeField howProvidedCodeField;

  private String id = UUID.randomUUID().toString();

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("aidType", "usage", "howProvidedCodeField");
  }
}
