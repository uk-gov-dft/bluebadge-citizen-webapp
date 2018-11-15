package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.config.S3Config;
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

  @PostMapping(value = "/prove-identity-ajax", produces = "application/json")
  public Boolean submitAjax(@RequestParam("document") Object document) {

    /*StandardMultipartHttpServletRequest doc = (StandardMultipartHttpServletRequest) document;
    try {
      System.out.println(doc.getPart("document").getName());
    } catch (Exception e) {
    }*/

    /*if(!document.isEmpty()) {
      byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64((document.substring(document.indexOf(",") + 1)).getBytes());
      InputStream fis = new ByteArrayInputStream(bI);
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(bI.length);
      metadata.setContentType("image/png");
      AmazonS3 s3 = new S3Config().amazonS3();
      String fileName = UUID.randomUUID().toString();
      s3.putObject("uk-gov-dft-test-applications-temp", fileName, fis, metadata);
    }*/

    return true;
  }

  @PostMapping(Mappings.URL_PROVE_IDENTITY)
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @RequestParam("document") MultipartFile document,
      @Valid @ModelAttribute("formRequest") ProveIdentityForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (!document.isEmpty()) {

      String bucketName = "uk-gov-dft-test-applications-temp";
      String keyName = UUID.randomUUID().toString() + "-" + document.getName();

      TransferManager tm =
          TransferManagerBuilder.standard()
              .withS3Client(new S3Config().amazonS3())
              .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
              .build();

      File file = new File(document.getOriginalFilename());

      try {
        document.transferTo(file);
      } catch (IOException e) {
        System.out.println(e);
      }

      Upload upload = tm.upload(bucketName, keyName, file);

      try {
        upload.waitForUploadResult();
      } catch (Exception e) {

      }

      // File file = new File("/Users/ali.ashik/Desktop/photo.jpg");
      //Upload upload = tm.upload(bucketName, keyName, file);

      /*ProgressListener progressListener =
          progressEvent ->
              System.out.println("Transferred bytes: " + progressEvent.getBytesTransferred());

      File file = new File(document.getOriginalFilename());
      PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file);

      try {
        document.transferTo(file);
        request.setGeneralProgressListener(progressListener);
        Upload upload = tm.upload(request);
        upload.waitForCompletion();
      } catch (Exception e) {
        System.out.println(e);
      }*/

      /*try {
      } catch (InterruptedException e) {
          System.out.println(e);
      }*/

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
