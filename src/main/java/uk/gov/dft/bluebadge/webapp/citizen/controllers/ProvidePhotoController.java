package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_MIME_TYPES;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ServiceException;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.FileUploaderOptions;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProvidePhotoForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;
import uk.gov.dft.bluebadge.webapp.citizen.service.UnsupportedMimetypeException;

@Controller
@Slf4j
public class ProvidePhotoController implements StepController {

  public static final String TEMPLATE = "provide-photo";
  private static final String DOC_BYPASS_URL = "provide-photo-bypass";
  public static final String PROVIDE_PHOTO_AJAX_URL = "/provide-photo-ajax";
  public static final String DOCUMENT = "document";

  private final RouteMaster routeMaster;
  private final ArtifactService artifactService;

  @Autowired
  ProvidePhotoController(RouteMaster routeMaster, ArtifactService artifactService) {
    this.routeMaster = routeMaster;
    this.artifactService = artifactService;
  }

  @GetMapping(Mappings.URL_PROVIDE_PHOTO)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      ProvidePhotoForm providePhotoForm = journey.getFormForStep(getStepDefinition());
      if (null != providePhotoForm.getJourneyArtifact()) {
        artifactService.createAccessibleLinks(providePhotoForm.getJourneyArtifact());
      }
      model.addAttribute(FORM_REQUEST, providePhotoForm);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ProvidePhotoForm.builder().build());
    }

    model.addAttribute("fileUploaderOptions", getFileUploaderOptions());

    return TEMPLATE;
  }

  @GetMapping(DOC_BYPASS_URL)
  public String formByPass(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    ProvidePhotoForm formRequest = ProvidePhotoForm.builder().build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private FileUploaderOptions getFileUploaderOptions() {
    return FileUploaderOptions.builder()
        .fieldName(DOCUMENT)
        .ajaxRequestUrl(PROVIDE_PHOTO_AJAX_URL)
        .fieldLabel("providePhoto.fu.field.label")
        .allowedFileTypes(String.join(",", IMAGE_MIME_TYPES))
        .allowMultipleFileUploads(false)
        .rejectErrorMessageKey("providePhoto.fu.rejected.content")
        .build();
  }

  @PostMapping(value = PROVIDE_PHOTO_AJAX_URL, produces = "application/json")
  @ResponseBody
  public Map<String, Object> submitAjax(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) MultipartFile document,
      ProvidePhotoForm providePhotoForm) {
    try {
      JourneyArtifact uploadedJourneyArtifact = artifactService.upload(document, IMAGE_MIME_TYPES);
      providePhotoForm.setJourneyArtifact(uploadedJourneyArtifact);
      journey.setFormForStep(providePhotoForm);
      return ImmutableMap.of("success", "true", "artifact", uploadedJourneyArtifact);
    } catch (Exception e) {
      log.warn("Failed to upload document through ajax call.", e);
      return ImmutableMap.of("error", "Failed to upload");
    }
  }

  @PostMapping(Mappings.URL_PROVIDE_PHOTO)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) MultipartFile document,
      @Valid @ModelAttribute(FORM_REQUEST) ProvidePhotoForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (!document.isEmpty()) {
      try {
        JourneyArtifact uploadJourneyArtifact = artifactService.upload(document, IMAGE_MIME_TYPES);
        formRequest.setJourneyArtifact(uploadJourneyArtifact);
        journey.setFormForStep(formRequest);
      } catch (UnsupportedMimetypeException e) {
        attr.addFlashAttribute(ArtifactService.UNSUPPORTED_FILE, Boolean.TRUE);
        return "redirect:" + Mappings.URL_PROVIDE_PHOTO;
      } catch (ServiceException e) {
        log.warn("Failed to upload document", e);
        bindingResult.rejectValue(DOCUMENT, "");
      }
    }

    ProvidePhotoForm sessionForm = journey.getFormForStep(getStepDefinition());
    if (null == sessionForm || !sessionForm.hasArtifacts()) {
      bindingResult.rejectValue("journeyArtifact", "providePhoto.NotNull.photo");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.PROVIDE_PHOTO;
  }
}
