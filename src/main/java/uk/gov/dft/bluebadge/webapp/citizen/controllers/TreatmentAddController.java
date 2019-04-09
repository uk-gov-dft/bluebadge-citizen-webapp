package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.TreatmentWhenType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;

@Slf4j
@Controller
@RequestMapping(Mappings.URL_TREATMENT_ADD)
public class TreatmentAddController implements StepController {

  private RouteMaster routeMaster;
  private static final String TEMPLATE = "treatment-add";

  @Autowired
  TreatmentAddController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious(journey);
    }

    // On returning to form, take previously submitted values.
    if (!model.containsAttribute(FORM_REQUEST)) {
      model.addAttribute(FORM_REQUEST, new TreatmentAddForm());
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute(FORM_REQUEST) TreatmentAddForm treatmentAddForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    // Validate conditional input fields
    if (null != treatmentAddForm.getTreatmentWhenType()) {
      String treatmentWhen = null;

      switch (treatmentAddForm.getTreatmentWhenType()) {
        case PAST:
          if (StringUtils.isEmpty(treatmentAddForm.getTreatmentPastWhen())) {
            bindingResult.rejectValue(
                "treatmentPastWhen", "NotNull.treatment.fields.treatmentPastWhen");
          } else if (treatmentAddForm.getTreatmentPastWhen().length() > 100) {
            bindingResult.rejectValue(
                "treatmentPastWhen", "Size.treatment.fields.treatmentPastWhen");
          } else {
            treatmentWhen = treatmentAddForm.getTreatmentPastWhen();
          }
          break;
        case ONGOING:
          if (StringUtils.isEmpty(treatmentAddForm.getTreatmentOngoingFrequency())) {
            bindingResult.rejectValue(
                "treatmentOngoingFrequency", "NotNull.treatment.fields.treatmentOngoingFrequency");
          } else if (treatmentAddForm.getTreatmentOngoingFrequency().length() > 90) {
            bindingResult.rejectValue(
                "treatmentOngoingFrequency", "Size.treatment.fields.treatmentOngoingFrequency");
          } else {
            treatmentWhen = "Ongoing - " + treatmentAddForm.getTreatmentOngoingFrequency();
          }
          break;
        case FUTURE:
          Boolean validWhenFound = true;

          if (StringUtils.isEmpty(treatmentAddForm.getTreatmentFutureWhen())) {
            bindingResult.rejectValue(
                "treatmentFutureWhen", "NotNull.treatment.fields.treatmentFutureWhen");
            validWhenFound = false;
          } else if (treatmentAddForm.getTreatmentFutureWhen().length() > 35) {
            bindingResult.rejectValue(
                "treatmentFutureWhen", "Size.treatment.fields.treatmentFutureWhen");
            validWhenFound = false;
          }

          if (StringUtils.isEmpty(treatmentAddForm.getTreatmentFutureImprove())) {
            bindingResult.rejectValue(
                "treatmentFutureImprove", "NotNull.treatment.fields.treatmentFutureImprove");
          } else if (treatmentAddForm.getTreatmentFutureImprove().length() > 25) {
            bindingResult.rejectValue(
                "treatmentFutureImprove", "Size.treatment.fields.treatmentFutureImprove");
          } else if (validWhenFound) {
            treatmentWhen =
                treatmentAddForm.getTreatmentFutureWhen()
                    + " - Expected to improve? "
                    + treatmentAddForm.getTreatmentFutureImprove();
          }
          break;
      }

      if (null != treatmentWhen) {
        treatmentAddForm.setTreatmentWhen(treatmentWhen);
      }
    }

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, treatmentAddForm, bindingResult, attr);
    }

    TreatmentListForm treatmentListForm = journey.getFormForStep(TREATMENT_LIST);
    if (null == treatmentListForm) {
      treatmentListForm = TreatmentListForm.builder().build();
    }

    treatmentListForm.setHasTreatment("yes");
    treatmentListForm.addTreatment(treatmentAddForm);
    journey.setFormForStep(treatmentListForm);

    return "redirect:" + Mappings.URL_TREATMENT_LIST;
  }

  @ModelAttribute("whenTypeCodes")
  public RadioOptionsGroup whenTypeCodes(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return null;
    }

    List<RadioOption> options = new ArrayList<>();
    options.add(new RadioOption(TreatmentWhenType.PAST, "treatment.add.when.past.title"));
    options.add(new RadioOption(TreatmentWhenType.ONGOING, "treatment.add.when.ongoing.title"));
    options.add(new RadioOption(TreatmentWhenType.FUTURE, "treatment.add.when.future.title"));

    return new RadioOptionsGroup("treatment.add.when.title", options);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.TREATMENT_ADD;
  }
}
