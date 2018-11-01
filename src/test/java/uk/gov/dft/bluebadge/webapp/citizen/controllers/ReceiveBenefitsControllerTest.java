package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;

public class ReceiveBenefitsControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  @Before
  public void setup() {
    ReceiveBenefitsController controller = new ReceiveBenefitsController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.RECEIVE_BENEFITS, EligibilityCodeField.WALKD);
  }

  @Test
  public void show_ShouldDisplayBenefitsTemplate() throws Exception {
    ReceiveBenefitsForm form = journey.getFormForStep(StepDefinition.RECEIVE_BENEFITS);

    mockMvc
        .perform(get("/benefits").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("receive-benefits"))
        .andExpect(model().attribute("formRequest", form))
        .andExpect(model().attribute("benefitOptions", Matchers.notNullValue()));
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/benefits"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post("/benefits")
                .param("benefitType", EligibilityCodeField.PIP.name())
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_PIP_MOVING_AROUND));
  }

  @Test
  public void submit_whenMissingBenefitsAnswer_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post("/benefits").sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_RECEIVE_BENEFITS + RouteMaster.ERROR_SUFFIX));
  }
}
