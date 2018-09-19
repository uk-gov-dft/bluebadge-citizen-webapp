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
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
@RequestMapping(Mappings.URL_CHOOSE_YOUR_COUNCIL)
public class ChooseYourCouncilController implements StepController {

  public static final String TEMPLATE = "choose-council";

  private ReferenceDataService referenceDataService;
  private final RouteMaster routeMaster;

  @Autowired
  public ChooseYourCouncilController(
      ReferenceDataService referenceDataService, RouteMaster routeMaster) {
    this.referenceDataService = referenceDataService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (null != journey.getChooseYourCouncilForm()) {
      model.addAttribute("formRequest", journey.getChooseYourCouncilForm());
    }

    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", ChooseYourCouncilForm.builder().build());
    }
    List<ReferenceData> councils =
        referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    model.addAttribute("councils", councils);
    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      @Valid @ModelAttribute("formRequest") ChooseYourCouncilForm chooseYourCouncilForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("errorSummary", new ErrorViewModel());
      attr.addFlashAttribute(
          "org.springframework.validation.BindingResult.formRequest", bindingResult);
      attr.addFlashAttribute("formRequest", chooseYourCouncilForm);
      return "redirect:/choose-council";
    }

    journey.setChooseYourCouncilForm(chooseYourCouncilForm);
    return routeMaster.redirectToOnSuccess(this);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.CHOOSE_COUNCIL;
  }
}
