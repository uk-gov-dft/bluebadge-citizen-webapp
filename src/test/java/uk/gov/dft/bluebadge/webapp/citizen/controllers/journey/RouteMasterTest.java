package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.DECLARATIONS;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HOME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.RECEIVE_BENEFITS;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class RouteMasterTest {
  RouteMaster routeMaster;
  @Before
  public void setup(){
    routeMaster = new RouteMaster();
  }

  @Test
  public void redirectOnSuccess_singleNextStep(){
    assertThat(routeMaster.redirectToOnSuccess(StepDefinition.HOME))
        .isEqualTo("redirect:" + Mappings.URL_APPLICANT_TYPE);
  }
  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccess_whenMultiple_thenException(){
    routeMaster.redirectToOnSuccess(StepDefinition.RECEIVE_BENEFITS);
  }

  @Test
  public void redirectOnSuccessWithForm_singleNextStep(){
    StepForm testForm = () -> HOME;

    assertThat(routeMaster.redirectToOnSuccess(testForm))
        .isEqualTo("redirect:" + Mappings.URL_APPLICANT_TYPE);
  }
  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccessWithForm_whenMultiple_thenExcpetion(){
    StepForm testForm = () -> RECEIVE_BENEFITS;

    routeMaster.redirectToOnSuccess(testForm);
  }
  @Test
  public void redirectOnSuccessWithForm_whenMultiple_thenFormDetermines(){
    StepForm testForm = new StepForm() {
      @Override
      public StepDefinition getAssociatedStep() {
        return RECEIVE_BENEFITS;
      }

      @Override
      public Optional<StepDefinition> determineNextStep() {
        return Optional.of(ELIGIBLE);
      }
    };

    assertThat(routeMaster.redirectToOnSuccess(testForm))
        .isEqualTo("redirect:" + Mappings.URL_ELIGIBLE);
  }
  @Test(expected = IllegalStateException.class)
  public void redirectOnSuccessWithForm_whenMultipleAndFormDeterminesInvalid_thenException(){
    StepForm testForm = new StepForm() {
      @Override
      public StepDefinition getAssociatedStep() {
        return RECEIVE_BENEFITS;
      }

      @Override
      public Optional<StepDefinition> determineNextStep() {
        return Optional.of(DECLARATIONS);
      }
    };

    assertThat(routeMaster.redirectToOnSuccess(testForm));
  }
}