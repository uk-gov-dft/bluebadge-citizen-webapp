package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney.SAVE_AND_RETURN_JOURNEY_KEY;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;

@SessionAttributes(SAVE_AND_RETURN_JOURNEY_KEY)
public interface SaveAndReturnController {

  @SuppressWarnings("unused")
  @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY)
  default SaveAndReturnJourney saveAndReturnJourney() {
    return new SaveAndReturnJourney();
  }
}
