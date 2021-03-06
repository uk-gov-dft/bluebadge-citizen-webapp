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
@SuppressWarnings({"squid:S3776"})
public class UploadSupportingDocumentsController implements StepController {

  public static final String TEMPLATE = "upload-supporting-documents";
  private static final String DOC_BYPASS_URL = "upload-supporting-documents-bypass";
  private static final String AJAX_URL = "/upload-supporting-documents-ajax";
  private static final Integer MAX_NUMBER_SUPPORTING_DOCUMENTS = 15;
  private static final String DOCUMENT = "document";
  private static final String CLEAR = "clear";

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
      return routeMaster.backToCompletedPrevious(journey);
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      UploadSupportingDocumentsForm form = journey.getFormForStep(getStepDefinition());
      if (null != form.getJourneyArtifacts()) {
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
  public String formByPass(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    UploadSupportingDocumentsForm formRequest =
        UploadSupportingDocumentsForm.builder()
            .hasDocuments(false)
            .journeyArtifacts(Lists.newArrayList())
            .build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private FileUploaderOptions getFileUploaderOptions() {
    return FileUploaderOptions.builder()
        .fieldName(DOCUMENT)
        .ajaxRequestUrl(AJAX_URL)
        .fieldLabel("uploadSupportingDocuments.fu.field.label")
        .maxFileUploadLimit(MAX_NUMBER_SUPPORTING_DOCUMENTS)
        .allowedFileTypes(String.join(",", IMAGE_PDF_MIME_TYPES))
        .allowMultipleFileUploads(true)
        .rejectErrorMessageKey("uploadSupportingDocuments.fu.rejected.content")
        .build();
  }

  @PostMapping(value = AJAX_URL, produces = "application/json")
  @ResponseBody
  public Map<String, Object> submitAjax(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) List<MultipartFile> documents,
      @RequestParam(name = CLEAR, defaultValue = "false") Boolean clearPreviousArtifacts,
      UploadSupportingDocumentsForm form) {
    try {
      UploadSupportingDocumentsForm sessionForm = journey.getOrSetFormForStep(form);

      if (clearPreviousArtifacts) {
        sessionForm.setJourneyArtifacts(new ArrayList<>());
      }

      List<JourneyArtifact> journeyArtifacts = Lists.newArrayList();
      if (countDocumentsAfterUploading(sessionForm, documents) <= MAX_NUMBER_SUPPORTING_DOCUMENTS) {
        journeyArtifacts.addAll(artifactService.upload(documents, IMAGE_PDF_MIME_TYPES));
      } else {
        log.info(
            "Uploading the given documents will reach more than the total number allowed: "
                + MAX_NUMBER_SUPPORTING_DOCUMENTS
                + ".");

        return ImmutableMap.of("error", "upload limit exceeded");
      }
      if (!journeyArtifacts.isEmpty() && sessionForm != null) {
        sessionForm.getJourneyArtifacts().addAll(journeyArtifacts);
        sessionForm.setHasDocuments(true);
      }
      return ImmutableMap.of("success", "true", "artifact", journeyArtifacts);
    } catch (Exception e) {
      log.warn("Failed to upload document through ajax call.", e);
      return ImmutableMap.of("error", "Failed to upload");
    }
  }

  private long countDocumentsAfterUploading(
      UploadSupportingDocumentsForm sessionForm, List<MultipartFile> documents) {
    long sessionFormDocumentsSize =
        (sessionForm != null && sessionForm.getJourneyArtifacts() != null
            ? sessionForm.getJourneyArtifacts().size()
            : 0);
    long documentsSize =
        (documents != null ? documents.stream().filter(doc -> !doc.isEmpty()).count() : 0);
    return sessionFormDocumentsSize + documentsSize;
  }

  @PostMapping(Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) List<MultipartFile> documents,
      @Valid @ModelAttribute(FORM_REQUEST) UploadSupportingDocumentsForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    UploadSupportingDocumentsForm sessionForm = journey.getOrSetFormForStep(formRequest);

    if (formRequest.getHasDocuments() != null && !formRequest.getHasDocuments().booleanValue()) {
      sessionForm.setHasDocuments(Boolean.FALSE);
      sessionForm.setJourneyArtifacts(new ArrayList<>());
    }
    if (sessionForm.getHasDocuments() == null) {
      sessionForm.setHasDocuments(formRequest.getHasDocuments());
    }

    if (sessionForm.getHasDocuments() != null
        && sessionForm.getHasDocuments().booleanValue()
        && documents != null
        && !documents.isEmpty()) {
      if (countDocumentsAfterUploading(sessionForm, documents) <= MAX_NUMBER_SUPPORTING_DOCUMENTS) {
        try {
          List<JourneyArtifact> newArtifacts =
              artifactService.upload(documents, IMAGE_PDF_MIME_TYPES);
          if (!newArtifacts.isEmpty()) {
            sessionForm.setJourneyArtifacts(newArtifacts);
          }
        } catch (UnsupportedMimetypeException e) {
          attr.addFlashAttribute(ArtifactService.UNSUPPORTED_FILE, true);
          return "redirect:" + Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS;
        } catch (Exception e) {
          log.warn("Failed to upload document", e);
          bindingResult.rejectValue(DOCUMENT, "");
        }
      } else {
        attr.addFlashAttribute(ArtifactService.MAX_UPLOAD_LIMIT_REACHED, true);
        return "redirect:" + Mappings.URL_UPLOAD_SUPPORTING_DOCUMENTS;
      }
    }
    if (formRequest.getHasDocuments() == null || !formRequest.getHasDocuments()) {
      sessionForm.setJourneyArtifacts(Lists.newArrayList());
    }

    rejectIfShouldAttachADocument(formRequest, bindingResult, sessionForm);

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private void rejectIfShouldAttachADocument(
      @ModelAttribute(FORM_REQUEST) @Valid UploadSupportingDocumentsForm formRequest,
      BindingResult bindingResult,
      UploadSupportingDocumentsForm sessionForm) {
    if (formRequest.getHasDocuments() != null
        && formRequest.getHasDocuments().booleanValue()
        && sessionForm.getJourneyArtifacts().isEmpty()) {
      bindingResult.rejectValue(
          "journeyArtifact",
          "NotNull.uploadSupportingDocuments.document",
          "Supporting documents is required if you answer yes");
    }
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS;
  }
}
