package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService.IMAGE_PDF_MIME_TYPES;

import com.google.common.collect.ImmutableMap;
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
import uk.gov.dft.bluebadge.webapp.citizen.client.common.ServiceException;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.FileUploaderOptions;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadBenefitForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ArtifactService;
import uk.gov.dft.bluebadge.webapp.citizen.service.UnsupportedMimetypeException;

@Controller
@Slf4j
@SuppressWarnings({"squid:S2259", "squid:S3776"})
public class UploadBenefitController implements StepController {

  public static final String TEMPLATE = "upload-benefit";
  private static final String DOC_BYPASS_URL = "upload-benefit-bypass";
  public static final String UPLOAD_BENEFIT_AJAX_URL = "/upload-benefit-ajax";
  public static final String DOCUMENT = "document";
  public static final String CLEAR = "clear";
  private static final Integer MAX_NUMBER_SUPPORTING_DOCUMENTS = 15;

  private final RouteMaster routeMaster;
  private final ArtifactService artifactService;

  @Autowired
  UploadBenefitController(RouteMaster routeMaster, ArtifactService artifactService) {
    this.routeMaster = routeMaster;
    this.artifactService = artifactService;
  }

  @GetMapping(Mappings.URL_UPLOAD_BENEFIT)
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      UploadBenefitForm uploadBenefitForm = journey.getFormForStep(getStepDefinition());
      uploadBenefitForm.getJourneyArtifacts().forEach(artifactService::createAccessibleLinks);
      model.addAttribute(FORM_REQUEST, uploadBenefitForm);
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, UploadBenefitForm.builder().build());
    }

    model.addAttribute("fileUploaderOptions", getFileUploaderOptions());

    return TEMPLATE;
  }

  @GetMapping(DOC_BYPASS_URL)
  public String formByPass(@SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    UploadBenefitForm formRequest = UploadBenefitForm.builder().build();
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  private static FileUploaderOptions getFileUploaderOptions() {
    return FileUploaderOptions.builder()
        .fieldName(DOCUMENT)
        .ajaxRequestUrl(UPLOAD_BENEFIT_AJAX_URL)
        .fieldLabel("proveIdentity.fu.field.label")
        .allowedFileTypes(String.join(",", IMAGE_PDF_MIME_TYPES))
        .allowMultipleFileUploads(true)
        .maxFileUploadLimit(MAX_NUMBER_SUPPORTING_DOCUMENTS)
        .previewTitleMessageKey("fileUploader.multipleFile.preview.title")
        .rejectErrorMessageKey("upload.benefit.fu.rejected.content")
        .addFileMessageKey("upload.benefit.add.another")
        .build();
  }

  @PostMapping(value = UPLOAD_BENEFIT_AJAX_URL, produces = "application/json")
  @ResponseBody
  public Map<String, Object> submitAjax(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) List<MultipartFile> documents,
      @RequestParam(name = CLEAR, defaultValue = "false") Boolean clearPreviousArtifacts,
      UploadBenefitForm uploadBenefitForm) {
    try {
      UploadBenefitForm sessionUploadBenefitForm = journey.getOrSetFormForStep(uploadBenefitForm);

      if (clearPreviousArtifacts) {
        sessionUploadBenefitForm.setJourneyArtifacts(new ArrayList<>());
      }

      List<JourneyArtifact> newJourneyArtifacts =
          artifactService.upload(documents, IMAGE_PDF_MIME_TYPES);
      if (!newJourneyArtifacts.isEmpty()) {
        sessionUploadBenefitForm.getJourneyArtifacts().addAll(newJourneyArtifacts);
      }

      return ImmutableMap.of("success", "true", "artifact", newJourneyArtifacts);
    } catch (Exception e) {
      log.warn("Failed to upload document through ajax call.", e);
      return ImmutableMap.of("error", "Failed to upload");
    }
  }

  @PostMapping(Mappings.URL_UPLOAD_BENEFIT)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam(DOCUMENT) List<MultipartFile> documents,
      @Valid @ModelAttribute(FORM_REQUEST) UploadBenefitForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    UploadBenefitForm sessionForm = journey.getOrSetFormForStep(formRequest);

    if (!documents.isEmpty()) {
      if (countDocumentsAfterUploading(sessionForm, documents) <= MAX_NUMBER_SUPPORTING_DOCUMENTS) {
        try {
          List<JourneyArtifact> newArtifacts =
              artifactService.upload(documents, IMAGE_PDF_MIME_TYPES);
          if (!newArtifacts.isEmpty()) {
            sessionForm.setJourneyArtifacts(newArtifacts);
          }
        } catch (UnsupportedMimetypeException e) {
          attr.addFlashAttribute(ArtifactService.UNSUPPORTED_FILE, true);
          return "redirect:" + Mappings.URL_UPLOAD_BENEFIT;
        } catch (ServiceException e) {
          log.warn("Failed to upload document", e);
          bindingResult.rejectValue("journeyArtifact", "Empty.journeyArtifact");
        }
      } else {
        attr.addFlashAttribute(ArtifactService.MAX_UPLOAD_LIMIT_REACHED, true);
        return "redirect:" + Mappings.URL_UPLOAD_BENEFIT;
      }
    }
    if (null == sessionForm || !sessionForm.hasArtifacts()) {
      bindingResult.rejectValue("journeyArtifact", "NotNull.upload.benefit.document");
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.UPLOAD_BENEFIT;
  }

  private long countDocumentsAfterUploading(
      UploadBenefitForm sessionForm, List<MultipartFile> documents) {
    long sessionFormDocumentsSize =
        (sessionForm != null && sessionForm.getJourneyArtifacts() != null
            ? sessionForm.getJourneyArtifacts().size()
            : 0);
    long documentsSize =
        (documents != null ? documents.stream().filter(doc -> !doc.isEmpty()).count() : 0);
    return sessionFormDocumentsSize + documentsSize;
  }
}
