package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;

public abstract class ControllerTestFixture<T> {

  protected MockMvc mockMvc;
  protected Journey journey;

  protected void setup(T controller) {
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  static ResultMatcher formRequestFlashAttributeHasFieldErrorCode(String fieldName, String error) {
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

  private Journey getDefaultJourney() {
    Journey journey = new Journey();
    ApplicantForm applicantForm =
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.toString()).build();
    journey.setApplicantForm(applicantForm);
    return journey;
  }

  void applyRoutmasterDefaultMocks(RouteMaster mockRouteMaster) {
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("backToStart");
    when(mockRouteMaster.redirectToOnBindingError(any(), any(), any(), any())).thenCallRealMethod();
  }

  abstract String getTemplateName();

  abstract String getUrl();

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
        .andExpect(status().isOk())
        .andExpect(view().name("backToStart"));
  }
}
