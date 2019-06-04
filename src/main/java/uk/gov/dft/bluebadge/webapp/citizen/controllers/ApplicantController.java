package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.CookieUtils;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_APPLICANT_TYPE)
public class ApplicantController implements StepController {
  private static final String TEMPLATE_APPLICANT = "applicant";
  private final RouteMaster routeMaster;
  private final RedirectVersionCookieManager cookieManager;

  ApplicantController(RouteMaster routeMaster, RedirectVersionCookieManager cookieManager) {
    this.routeMaster = routeMaster;
    this.cookieManager = cookieManager;
  }

  @GetMapping
  public String show(
      Model model,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      HttpServletResponse response, HttpServletRequest request) {

    // Returning to previously visited page
    if (!model.containsAttribute(FORM_REQUEST)
        && journey.hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(StepDefinition.APPLICANT_TYPE));
    }

    // A new application
    if (!model.containsAttribute(FORM_REQUEST)) {
      Cookie versionCookie = cookieManager.getCookie(request);
      if (null != versionCookie) {
        log.debug("Removing version cookie {} and redirecting on creation of new application.", versionCookie.getName());
        cookieManager.removeCookie(response);
        return "redirect:" + Mappings.URL_APPLICANT_TYPE;
      } else {
        String newVersion = cookieManager.addCookie(response);
        log.debug("Created new application and adding version cookie for {}", newVersion);
      }
      model.addAttribute(FORM_REQUEST, ApplicantForm.builder().build());
    }

    RadioOptionsGroup applicantOptions = getApplicantOptions();
    model.addAttribute("applicantOptions", applicantOptions);

    return TEMPLATE_APPLICANT;
  }

  RadioOptionsGroup getApplicantOptions() {
    RadioOption yourself =
        new RadioOption(ApplicantType.YOURSELF.toString(), "options.applicantType.yourself");
    RadioOption someone =
        new RadioOption(ApplicantType.SOMEONE_ELSE.toString(), "options.applicantType.someone");
    RadioOption organisation =
        new RadioOption(
            ApplicantType.ORGANISATION.toString(), "options.applicantType.organisation");

    return new RadioOptionsGroup(
        "applicantPage.title", Lists.newArrayList(yourself, someone, organisation));
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ApplicantForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr, HttpServletRequest request) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.APPLICANT_TYPE;
  }
}
