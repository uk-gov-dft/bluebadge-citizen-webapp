package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;

@Controller
public class ProveIdentityController implements StepController {

  public static final String TEMPLATE = "prove-identity";

  private final RouteMaster routeMaster;

  @Autowired
  ProveIdentityController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping(Mappings.URL_PROVE_IDENTITY)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ProveIdentityForm.builder().build());
    }

    return TEMPLATE;
  }

  @PostMapping(Mappings.URL_PROVE_IDENTITY)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      HttpServletRequest request,
      @Valid @ModelAttribute("formRequest") ProveIdentityForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
    MultipartFile file = multiRequest.getFile("document");

    if (!file.getOriginalFilename().isEmpty()) {
      // make s3 upload
      // https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
      formRequest.setDocumentId("some-id-from-aws");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setFormForStep(formRequest);

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PROVE_IDENTITY;
  }
}
