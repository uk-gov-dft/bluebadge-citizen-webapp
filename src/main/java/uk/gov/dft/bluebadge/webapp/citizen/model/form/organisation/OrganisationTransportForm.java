package uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation;

import java.io.Serializable;
import java.util.Optional;

import javax.validation.constraints.AssertTrue;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_NOT_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_TRANSPORT;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class OrganisationTransportForm implements StepForm, Serializable{

	@AssertTrue(message = "{organisationTransport.page.validation.select}")
	private Boolean doesTransport;

	public StepDefinition getAssociatedStep() {
		return ORGANISATION_TRANSPORT;
	}

	@Override
	public Optional<StepDefinition> determineNextStep() {
		return doesTransport ? Optional.of(ORGANISATION_MAY_BE_ELIGIBLE) : Optional.of(ORGANISATION_NOT_ELIGIBLE);
	}

	
}
