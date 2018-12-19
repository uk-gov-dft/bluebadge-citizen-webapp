package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_PDF_MIME_TYPES;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadSupportingDocumentsForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;
import uk.gov.dft.bluebadge.webapp.citizen.service.UnsupportedMimetypeException;

@Controller
@Slf4j
public class UploadSupportingDocumentsController implements StepController {

  public static final String TEMPLATE = "upload-supporting-documents";
  private static final String DOC_BYPASS_URL = "upload-supporting-documents-bypass";
  public static final String AJAX_URL = "/upload-supporting-documents-ajax";

  private final RouteMaster routeMaster;
  private final ArtifactService artifactService;

  @Autowired
  UploadSupportingDocumentsController(RouteMaster routeMaster, ArtifactService artifactService) {
    this.routeMaster = routeMaster;
    this.artifactService = artifactService;
  }

  @GetMapping(Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      UploadSupportingDocumentsForm form = journey.getFormForStep(getStepDefinition());
      if (null != form.getJourneyArtifact()) {
        form.getJourneyArtifacts().forEach(artifactService::createAccessibleLinks);
      }
      model.addAttribute(FORM_REQUEST, form);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, UploadSupportingDocumentsForm.builder().build());
    }

    model.addAttribute("fileUploaderOptions", getFileUploaderOptions());

    return TEMPLATE;
  }

  @GetMapping(DOC_BYPASS_URL)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    UploadSupportingDocumentsForm formRequest =
        UploadSupportingDocumentsForm.builder().hasDocuments(false).journeyArtifacts(Lists.newArrayList()).build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private FileUploaderOptions getFileUploaderOptions() {
    return FileUploaderOptions.builder()
        .fieldName("document")
        .ajaxRequestUrl(AJAX_URL)
        .fieldLabel("uploadSupportingDocuments.fu.field.label")
        .allowedFileTypes("image/jpeg,image/gif,image/png,application/pdf")
        .allowMultipleFileUploads(true)
        .rejectErrorMessageKey("uploadSupportingDocuments.fu.rejected.content")
        .build();
  }

  @PostMapping(value = AJAX_URL, produces = "application/json")
  @ResponseBody
  public Map<String, Object> submitAjax(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam("document") List<MultipartFile> documents,
      @RequestParam(name = "clear", defaultValue = "false") Boolean clearPreviousArtifacts,
      UploadSupportingDocumentsForm form) {
    try {
      UploadSupportingDocumentsForm sessionForm = journey.getOrSetFormForStep(form);

      if (clearPreviousArtifacts) {
        sessionForm.setJourneyArtifacts(new ArrayList<>());
      }

      List<JourneyArtifact> journeyArtifacts = new ArrayList<>();
      for (MultipartFile doc : documents) {
        JourneyArtifact journeyArtifact = artifactService.upload(doc, IMAGE_PDF_MIME_TYPES);
        sessionForm.addJourneyArtifact(journeyArtifact);
        journeyArtifacts.add(journeyArtifact);
      }

      return ImmutableMap.of("success", "true", "artifact", journeyArtifacts);
    } catch (Exception e) {
      log.warn("Failed to upload document through ajax call.", e);
      return ImmutableMap.of("error", "Failed to upload");
    }
  }

  @PostMapping(Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam("document") List<MultipartFile> documents,
      @Valid @ModelAttribute("formRequest") UploadSupportingDocumentsForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    UploadSupportingDocumentsForm sessionForm = journey.getOrSetFormForStep(formRequest);

    if (formRequest.getHasDocuments() != null && formRequest.getHasDocuments() && documents != null && !documents.isEmpty()) {
      try {
        List<JourneyArtifact> newArtifacts = new ArrayList<>();
        for (MultipartFile document : documents) {
          if (!document.isEmpty()) {
            JourneyArtifact uploadJourneyArtifact =
                artifactService.upload(document, IMAGE_PDF_MIME_TYPES);
            newArtifacts.add(uploadJourneyArtifact);
          }
        }
        if (!newArtifacts.isEmpty()) {
          sessionForm.setJourneyArtifacts(newArtifacts);
        }
      } catch (UnsupportedMimetypeException e) {
        attr.addFlashAttribute("MAX_FILE_SIZE_EXCEEDED", true);
        return "redirect:" + Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS;
      } catch (Exception e) {
        log.warn("Failed to upload document", e);
        bindingResult.rejectValue("document", "", "Failed to upload document");
      }
    }
    if (formRequest.getHasDocuments() == null || !formRequest.getHasDocuments()) {
      sessionForm.setJourneyArtifacts(Lists.newArrayList());
    }

    /*    if (null == sessionForm
      || null == sessionForm.getJourneyArtifacts()
      || sessionForm.getJourneyArtifacts().isEmpty()) {
      bindingResult.rejectValue(
        "journeyArtifact",
        "NotNull.upload.benefit.document",
        "Supporting documents is required");
    }*/

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS;
  }
}
