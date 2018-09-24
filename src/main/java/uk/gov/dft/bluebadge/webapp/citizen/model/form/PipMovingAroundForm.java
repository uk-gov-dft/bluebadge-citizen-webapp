package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_0;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_10;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_12;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_4;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_8;

@Data
@Builder
public class PipMovingAroundForm implements Serializable, StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    // TODO
    return Optional.of(StepDefinition.ELIGIBLE);
  }

  public enum PipMovingAroundOption implements Option {
    MOVING_POINTS_12("pip.moving.around.12"),
    MOVING_POINTS_10("pip.moving.around.12"),
    MOVING_POINTS_8("pip.moving.around.12"),
    MOVING_POINTS_4("pip.moving.around.12"),
    MOVING_POINTS_0("pip.moving.around.12");

    private String messageKey;

    PipMovingAroundOption(String messageKey) {

      this.messageKey = messageKey;
    }

    @Override
    public String getShortCode() {
      return this.name();
    }

    @Override
    public String getMessageKey() {
      return messageKey;
    }
  }

  private PipMovingAroundOption movingAroundPoints;

  public static final List<Option> options =
      Lists.newArrayList(
          MOVING_POINTS_12, MOVING_POINTS_10, MOVING_POINTS_8, MOVING_POINTS_4, MOVING_POINTS_0);
}
