package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.First;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.Second;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns;

@Data
@Builder
@GroupSequence({ EnterAddressForm.class, First.class, Second.class })
public class EnterAddressForm implements StepForm, Serializable {

  @NotBlank(groups= First.class, message = "{NotNull.buildingAndStreet}")
  @Size(groups= Second.class,max = 100, min = 1, message = "{Size.buildingAndStreet}")
  private String buildingAndStreet;

  @Size(groups= { First.class, Second.class}, max = 100, message = "{Size.optionalAddress}")
  private String optionalAddress;

  @NotBlank(groups= First.class, message = "{NotNull.townOrCity}")
  @Size(groups= Second.class,max = 100, min = 1, message = "{Size.townOrCity}")
  private String townOrCity;

  @NotBlank(groups= First.class, message = "{NotNull.postcode}")
  @Pattern(groups= Second.class,
    regexp = ValidationPatterns.POSTCODE_CASE_INSENSITIVE,
    message = "{Pattern.postcode}"
  )
  private String postcode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ADDRESS;
  }
}
