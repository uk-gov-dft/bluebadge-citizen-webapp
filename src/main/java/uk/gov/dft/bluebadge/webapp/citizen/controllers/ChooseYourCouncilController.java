package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
@RequestMapping(Mappings.URL_CHOOSE_YOUR_COUNCIL)
public class ChooseYourCouncilController implements StepController {

  private static final String TEMPLATE = "choose-council";
  public static final String FORM_REQUEST = "formRequest";

  private ReferenceDataService referenceDataService;
  private final RouteMaster routeMaster;

  @Autowired
  ChooseYourCouncilController(ReferenceDataService referenceDataService, RouteMaster routeMaster) {
    this.referenceDataService = referenceDataService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute(FORM_REQUEST) && journey.hasStepForm(getStepDefinition())) {
      model.addAttribute(FORM_REQUEST, journey.getFormForStep(getStepDefinition()));
    }

    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, ChooseYourCouncilForm.builder().build());
    }

    List<ReferenceData> councils =
        referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    //List<ReferenceData> activeCouncils = getActiveCouncils(councils);
    //    model.addAttribute("councils", activeCouncils);
    model.addAttribute("councils", councils);

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) ChooseYourCouncilForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    journey.setLocalAuthority(
        referenceDataService.lookupLocalAuthorityFromCouncilCode(
            formRequest.getCouncilShortCode()));
    journey.setFormForStep(formRequest);
    return routeMaster.redirectToOnSuccess(formRequest, journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.CHOOSE_COUNCIL;
  }

  /*
   * Returns the active ones from the list of the passed councils.
   */
  /*
  private List<ReferenceData> getActiveCouncils(List<ReferenceData> councils) {
    return councils
        .stream()
        .filter(
            council ->
                ((LocalCouncilRefData) council)
                    .getLocalCouncilMetaData()
                    .map(LocalCouncilRefData.LocalCouncilMetaData::getDifferentServiceSignpostUrl)
                  .map(StringUtils::isBlank)
                    .orElse(false))
        .collect(Collectors.toList());
  }*/
}
