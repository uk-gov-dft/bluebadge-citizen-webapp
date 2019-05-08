package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;

@SessionAttributes(SaveAndReturnController.SAVE_AND_RETURN_JOURNEY_KEY)
public interface SaveAndReturnController {
  String SAVE_AND_RETURN_JOURNEY_KEY = "saveAndReturnJourney";

  @SuppressWarnings("unused")
  @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY)
  default SaveAndReturnJourney saveAndReturnJourney() {
    return new SaveAndReturnJourney();
  }
}
