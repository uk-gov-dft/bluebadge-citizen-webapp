package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

import java.util.List;

@Controller
public class ChooseYourCouncilController {

  public static final String TEMPLATE = "choose-council";

  @Autowired
  private ReferenceDataService referenceDataService;

  public ChooseYourCouncilController() {}

  @GetMapping("choose-council")
  public String show(Model model, @ModelAttribute("formRequest") ChooseYourCouncilForm formRequest) {

    List<ReferenceData> councils = referenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL);
    model.addAttribute("councils", councils);
    return TEMPLATE;
  }
}
