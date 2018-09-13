package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;

public class RouteMaster {

  public static String redirectToOnSuccess(StepController currentStep){
    return "redirect:" + currentStep.getStepDefinition().getToUrl();
  }
}
