package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Controller
public class ApplicantController extends BaseController {

  private static final String URL_APPLICANT = "/applicant";
  private static final String TEMPLATE_APPLICANT = "applicant";
  public static final String JOURNEY_SESSION_KEY = "JOURNEY";

  @GetMapping(URL_APPLICANT)
  public String show(Model model, @ModelAttribute("formRequest") ApplicantForm formRequest) {

    List<ReferenceData> applicantOptions = getApplicantOptions();
    model.addAttribute("applicantOptions", applicantOptions);

    return TEMPLATE_APPLICANT;
  }

  private List<ReferenceData> getApplicantOptions() {
    ReferenceData yourself = new ReferenceData();
    yourself.setShortCode(ApplicantType.YOURSELF.toString());
    yourself.setDescription("Yourself");

    ReferenceData someone = new ReferenceData();
    someone.setShortCode(ApplicantType.SOMEONE_ELSE.toString());
    someone.setDescription("Someone else");

    return Lists.newArrayList(yourself, someone);
  }

  @PostMapping(URL_APPLICANT)
  public String submit(
      @SessionAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") ApplicantForm formRequest,
      BindingResult bindingResult,
      Model model) {

    model.addAttribute("errorSummary", new ErrorViewModel());

    if (bindingResult.hasErrors()) {
      List<ReferenceData> applicantOptions = getApplicantOptions();
      model.addAttribute("applicantOptions", applicantOptions);
      return TEMPLATE_APPLICANT;
    }

    journey.setApplicantForm(formRequest);

    return "redirect:" + DeclarationSubmitController.URL_DECLARATION;
  }
}
