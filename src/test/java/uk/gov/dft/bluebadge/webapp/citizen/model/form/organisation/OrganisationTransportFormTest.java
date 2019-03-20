package uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

@Slf4j
public class OrganisationTransportFormTest {
  @BeforeEach
  void beforeEachTest(TestInfo testInfo) {
    log.info(String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  void afterEachTest(TestInfo testInfo) {
    log.info(String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }

  @Test
  @DisplayName("Should navigate to organisation may be eligible form")
  public void determineNextStep_whenYes() {
    OrganisationTransportForm form =
        OrganisationTransportForm.builder().doesTransport(true).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get())
        .isEqualTo(StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE);
  }

  @Test
  @DisplayName("Should navigate to organisation not eligible form")
  public void determineNextStep_whenNo() {
    OrganisationTransportForm form =
        OrganisationTransportForm.builder().doesTransport(false).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ORGANISATION_NOT_ELIGIBLE);
  }
}
