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
public class OrganisationCareFormTest {
  @BeforeEach
  void beforeEachTest(TestInfo testInfo) {
    log.info(String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  void afterEachTest(TestInfo testInfo) {
    log.info(String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }

  @Test
  @DisplayName("Should navigate to organisation transport form")
  public void determineNextStep_whenYes() {
    OrganisationCareForm form = OrganisationCareForm.builder().doesCare(true).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get()).isEqualTo(StepDefinition.ORGANISATION_TRANSPORT);
  }

  @Test
  @DisplayName("Should navigate to organisation not eligible form")
  public void determineNextStep_whenNo() {
    OrganisationCareForm form = OrganisationCareForm.builder().doesCare(false).build();

    assertThat(form.determineNextStep(null)).isNotEmpty();
    assertThat(form.determineNextStep(null).get())
        .isEqualTo(StepDefinition.ORGANISATION_NOT_ELIGIBLE);
  }
}
