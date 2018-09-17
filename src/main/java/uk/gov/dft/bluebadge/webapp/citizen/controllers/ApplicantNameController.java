package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinitionEnum;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;

@Controller
public class ApplicantNameController implements StepController {

  public static final String TEMPLATE_APPLICANT_NAME = "applicant-name";

  @GetMapping(Mappings.URL_APPLICANT_NAME)
  public String show(
      @ModelAttribute("formRequest") ApplicantNameForm applicantNameForm,
      @SessionAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (null != journey.getApplicantNameForm()) {
      BeanUtils.copyProperties(journey.getApplicantNameForm(), applicantNameForm);
    }

    return TEMPLATE_APPLICANT_NAME;
  }

  @PostMapping(Mappings.URL_APPLICANT_NAME)
  public String submit(
      @SessionAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") ApplicantNameForm applicantNameForm,
      BindingResult bindingResult,
      Model model) {

    if (!applicantNameForm.isBirthNameValid()) {
      bindingResult.rejectValue("birthName", "field.birthName.NotBlank");
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("errorSummary", new ErrorViewModel());
      return TEMPLATE_APPLICANT_NAME;
    }

    if (!applicantNameForm.getHasBirthName() && applicantNameForm.getBirthName() != null) {
      applicantNameForm.setBirthName(null);
    }

    journey.setApplicantNameForm(applicantNameForm);
    return RouteMaster.redirectToOnSuccess(this);
  }

  @Override
  public StepDefinitionEnum getStepDefinition() {
    return StepDefinitionEnum.NAME;
  }
}
