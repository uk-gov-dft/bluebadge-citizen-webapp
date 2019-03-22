package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

@SessionAttributes(Journey.JOURNEY_SESSION_KEY)
public interface StepController {

  StepDefinition getStepDefinition();

  /**
   * Binds journey domain object into the session
   *
   * @return
   */
  @ModelAttribute(Journey.JOURNEY_SESSION_KEY)
  default Journey getJourney() {
    return new Journey();
  }

  @ModelAttribute("fieldsOrder")
  default List<String> getFieldsOrder(Model model) {
    BaseForm formRequest = (BaseForm) model.asMap().get("formRequest");
    if (formRequest != null) {
      return formRequest.getFieldOrder();
    } else {
      return Lists.newArrayList();
    }
  }
}
