package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.POSTCODE_CASE_INSENSITIVE;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class EnterAddressForm implements StepForm, Serializable {

  @NotBlank
  @Size(max = 100)
  private String buildingAndStreet;

  @Size(max = 100)
  private String optionalAddress;

  @NotBlank
  @Size(max = 100)
  private String townOrCity;

  @Pattern(regexp = POSTCODE_CASE_INSENSITIVE)
  private String postcode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ADDRESS;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }

  @Override
  public List<String> getFieldOrder() {
    return ImmutableList.of("buildingAndStreet", "optionalAddress", "townOrCity", "postcode");
  }
}
