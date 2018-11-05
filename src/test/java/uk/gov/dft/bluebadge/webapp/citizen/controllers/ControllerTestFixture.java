package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public abstract class ControllerTestFixture<T> {

  protected MockMvc mockMvc;
  protected Journey journey;

  protected abstract String getTemplateName();

  protected abstract String getUrl();

  protected abstract StepDefinition getStep();

  protected abstract EligibilityCodeField getEligibilityType();

  protected void setup(T controller) {
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(getStep(), getEligibilityType());
  }

  public static ResultMatcher formRequestFlashAttributeHasFieldErrorCode(
      String fieldName, String error) {
    return flash()
        .attribute(
            "org.springframework.validation.BindingResult.formRequest",
            hasProperty(
                "fieldErrors",
                hasItem(
                    allOf(
                        hasProperty("field", equalTo(fieldName)),
                        hasProperty("code", equalTo(error))))));
  }
  public static ResultMatcher formRequestFlashAttributeCount(int expectedErrorCount) {
    return flash()
        .attribute(
            "org.springframework.validation.BindingResult.formRequest",
            hasProperty(
                "fieldErrors",
                hasSize(expectedErrorCount)));
  }

  protected void show_ShouldDisplayTemplate() throws Exception {
    mockMvc
        .perform(get(getUrl()).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(getTemplateName()))
        .andExpect(model().attributeExists(Journey.FORM_REQUEST));
  }

  protected void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    mockMvc
        .perform(get(getUrl()).sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }
}
