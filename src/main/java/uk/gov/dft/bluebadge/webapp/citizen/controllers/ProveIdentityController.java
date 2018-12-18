package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.FileUploaderOptions;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;
import uk.gov.dft.bluebadge.webapp.citizen.service.UnsupportedMimetypeException;

@Controller
@Slf4j
public class ProveIdentityController implements StepController {

  public static final String TEMPLATE = "prove-identity";
  private static final String DOC_BYPASS_URL = "prove-id-bypass";
  public static final String PROVE_IDENTITY_AJAX_URL = "/prove-identity-ajax";

  private final RouteMaster routeMaster;
  private final ArtifactService artifactService;

  @Autowired
  ProveIdentityController(RouteMaster routeMaster, ArtifactService artifactService) {
    this.routeMaster = routeMaster;
    this.artifactService = artifactService;
  }

  @GetMapping(Mappings.URL_PROVE_IDENTITY)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      ProveIdentityForm proveIdentityForm = journey.getFormForStep(getStepDefinition());
      if (null != proveIdentityForm.getJourneyArtifact()) {
        artifactService.createAccessibleLinks(proveIdentityForm.getJourneyArtifact());
      }
      model.addAttribute(FORM_REQUEST, proveIdentityForm);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ProveIdentityForm.builder().build());
    }

    model.addAttribute("fileUploaderOptions", getFileUploaderOptions());

    return TEMPLATE;
  }

  @GetMapping(DOC_BYPASS_URL)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    ProveIdentityForm formRequest = ProveIdentityForm.builder().build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest);
  }

  private FileUploaderOptions getFileUploaderOptions() {
    return FileUploaderOptions.builder()
        .fieldName("document")
        .ajaxRequestUrl(PROVE_IDENTITY_AJAX_URL)
        .fieldLabel("proveIdentity.fu.field.label")
        .allowedFileTypes("image/jpeg,image/gif,image/png,application/pdf")
        .allowMultipleFileUploads(false)
        .rejectErrorMessageKey("proveIdentity.fu.rejected.content")
        .build();
  }

  @PostMapping(value = PROVE_IDENTITY_AJAX_URL, produces = "application/json")
  @ResponseBody
  public Map<String, Object> submitAjax(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam("document") MultipartFile document,
      ProveIdentityForm proveIdentityForm) {
    try {
      JourneyArtifact uploadedJourneyArtifact = artifactService.upload(document);
      proveIdentityForm.setJourneyArtifact(uploadedJourneyArtifact);
      journey.setFormForStep(proveIdentityForm);
      return ImmutableMap.of("success", "true", "artifact", uploadedJourneyArtifact);
    } catch (Exception e) {
      log.warn("Failed to upload document through ajax call.", e);
      return ImmutableMap.of("error", "Failed to upload");
    }
  }

  @PostMapping(Mappings.URL_PROVE_IDENTITY)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam("document") MultipartFile document,
      @Valid @ModelAttribute("formRequest") ProveIdentityForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (!document.isEmpty()) {
      try {
        JourneyArtifact uploadJourneyArtifact = artifactService.upload(document);
        formRequest.setJourneyArtifact(uploadJourneyArtifact);
        journey.setFormForStep(formRequest);
      } catch (UnsupportedMimetypeException e) {
        attr.addFlashAttribute("MAX_FILE_SIZE_EXCEEDED", true);
        return "redirect:" + Mappings.URL_PROVE_IDENTITY;
      } catch (Exception e) {
        log.warn("Failed to upload document", e);
        bindingResult.rejectValue("document", "", "Failed to upload document");
      }
    }

    ProveIdentityForm sessionForm = journey.getFormForStep(getStepDefinition());
    if (null == sessionForm || null == sessionForm.getJourneyArtifact()) {
      bindingResult.rejectValue(
          "journeyArtifact", "NotNull.document", "Prove of identity is required");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PROVE_IDENTITY;
  }
}
