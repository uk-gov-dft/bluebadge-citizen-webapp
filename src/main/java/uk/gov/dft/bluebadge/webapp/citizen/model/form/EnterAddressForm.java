package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns;

@Data
@Builder
public class EnterAddressForm implements StepForm, Serializable {

  @NotBlank(message = "{NotNull.buildingAndStreet}")
  @Size(max = 100, min = 1, message = "{Size.buildingAndStreet}")
  private String buildingAndStreet;

  @Size(max = 100, message = "{Size.optionalAddress}")
  private String optionalAddress;

  @Size(max = 100, min = 1, message = "{Size.townOrCity}")
  @NotBlank(message = "{NotNull.townOrCity}")
  private String townOrCity;

  @NotBlank(message = "{NotNull.postcode}")
  @Pattern(
    regexp = ValidationPatterns.POSTCODE_CASE_INSENSITIVE,
    message = "{Pattern.postcode}"
  )
  private String postcode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ADDRESS;
  }
}
