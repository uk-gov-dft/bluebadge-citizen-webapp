package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;

@Controller
@RequestMapping(Mappings.URL_CONTACT_DETAILS)
public class ContactDetailsController implements StepController {

  private static final String TEMPLATE_APPLICANT_NAME = "contact-details";
  public static final String FORM_REQUEST = "formRequest";

  private final RouteMaster routeMaster;

  @Autowired
  public ContactDetailsController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    //On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST) && null != journey.getContactDetailsForm()) {
      model.addAttribute(FORM_REQUEST, journey.getContactDetailsForm());
    }

    // If navigating forward from previous form, reset
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ContactDetailsForm.builder().build());
    }

    return TEMPLATE_APPLICANT_NAME;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ContactDetailsForm contactDetailsForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    // Validation context sensitive to journey
    if (contactDetailsForm.isFullnameInvalid(journey)) {
      bindingResult.rejectValue("fullName", "Invalid.contact.fullName");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, contactDetailsForm, bindingResult, attr);
    }

    journey.setContactDetailsForm(contactDetailsForm);
    return routeMaster.redirectToOnSuccess(contactDetailsForm);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.CONTACT_DETAILS;
  }
}
