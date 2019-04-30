package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.errorhandler.ErrorControllerAdvice.REDIRECT;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster.ERROR_SUFFIX;

@SessionAttributes(SaveAndReturnController.SAVE_AND_RETURN_JOURNEY_KEY)
public interface SaveAndReturnController {
  String SAVE_AND_RETURN_JOURNEY_KEY = "saveAndReturnJourney";

  @ModelAttribute(SAVE_AND_RETURN_JOURNEY_KEY)
  default SaveAndReturnJourney saveAndReturnJourney() {
    return new SaveAndReturnJourney();
  }

  default String redirectToOnBindingError(
      String url, Object formRequest, BindingResult bindingResult, RedirectAttributes attr) {
    attr.addFlashAttribute("errorSummary", new ErrorViewModel());
    attr.addFlashAttribute(
        "org.springframework.validation.BindingResult.formRequest", bindingResult);
    attr.addFlashAttribute("formRequest", formRequest);
    return REDIRECT + url + ERROR_SUFFIX;
  }
}
