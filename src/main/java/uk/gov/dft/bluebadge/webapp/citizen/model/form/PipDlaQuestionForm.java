package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.PipDlaQuestionForm.PipReceivedDlaOption.NEVER_RECEIVED_DLA;

@Data
@Builder
public class PipDlaQuestionForm implements Serializable, StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    // TODO
    return Optional.of(StepDefinition.ELIGIBLE);
  }

  public enum PipReceivedDlaOption implements Option {
    HAS_RECEIVED_DLA("pip.moving.around.12"),
    NEVER_RECEIVED_DLA("pip.moving.around.12");

    private String messageKey;

    PipReceivedDlaOption(String messageKey) {

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

  private PipMovingAroundForm.PipMovingAroundOption movingAroundPoints;

  public static final List<Option> options =
      Lists.newArrayList(HAS_RECEIVED_DLA, NEVER_RECEIVED_DLA);
}
