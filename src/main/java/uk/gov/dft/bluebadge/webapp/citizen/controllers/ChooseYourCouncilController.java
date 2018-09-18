package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
public class ChooseYourCouncilController {

  public static final String TEMPLATE = "choose-council";

  @Autowired private ReferenceDataService referenceDataService;

  @GetMapping("choose-council")
  public String show(
      Model model, @ModelAttribute("formRequest") ChooseYourCouncilForm formRequest) {

    List<ReferenceData> councils =
        referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    model.addAttribute("councils", councils);
    return TEMPLATE;
  }

  @PostMapping("choose-council")
  public String submit(
          Model model,
          @Valid @ModelAttribute("formRequest") ChooseYourCouncilForm formRequest,
          BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("errorSummary", new ErrorViewModel());
      return TEMPLATE;
    }

    return "redirect:/apply-for-a-blue-badge/declaration";
  }
}
