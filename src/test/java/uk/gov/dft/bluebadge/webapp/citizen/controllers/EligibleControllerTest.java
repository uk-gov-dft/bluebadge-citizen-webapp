package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;

public class EligibleControllerTest {

  private MockMvc mockMvc;

  @Before
  public void setup() {
    EligibleController controller = new EligibleController(RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  @SneakyThrows
  public void show_ShouldDisplayEligibleTemplate() {

    mockMvc
        .perform(
            get("/eligible")
                .sessionAttr(
                    "JOURNEY", new JourneyBuilder().withEligibility(BLIND).inEngland().build()))
        .andExpect(view().name("eligible"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("formRequest", Matchers.nullValue()));
  }

  @Test
  @SneakyThrows
  public void whenIssuingFormNotSet_thenRedirectBackToStart() {
    mockMvc
        .perform(
            get("/eligible")
                .sessionAttr(
                    "JOURNEY",
                    JourneyFixture.getDefaultJourneyToStep(StepDefinition.APPLICANT_TYPE)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  @SneakyThrows
  public void startApplication_ShouldRedirectToNextPage() {

    mockMvc
        .perform(
            get("/eligible/start")
                .sessionAttr(
                    "JOURNEY", JourneyFixture.getDefaultJourneyToStep(StepDefinition.ELIGIBLE)))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_APPLICANT_NAME));
  }
}
