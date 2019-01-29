package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class MayBeEligibleControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    MayBeEligibleController controller = new MayBeEligibleController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.MAIN_REASON, EligibilityCodeField.CHILDBULK);
  }

  @Test
  @SneakyThrows
  public void show_ShouldDisplayMayBeEligibleTemplate() {

    mockMvc
        .perform(get("/may-be-eligible").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("may-be-eligible"))
        .andExpect(model().attribute("formRequest", Matchers.nullValue()));
  }

  @Test
  @SneakyThrows
  public void whenIssuingFormNotSet_thenRedirectBackToStart() {
    mockMvc
        .perform(
            get("/may-be-eligible")
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
        .perform(get("/may-be-eligible/start"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_APPLICANT_NAME));
  }
}
