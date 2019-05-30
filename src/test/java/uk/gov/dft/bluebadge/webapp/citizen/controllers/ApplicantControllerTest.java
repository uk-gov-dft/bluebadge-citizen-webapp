package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.VersionCookieUtils;

public class ApplicantControllerTest {

  private MockMvc mockMvc;
  private ApplicantController controller;
  @Mock private RouteMaster mockRouteMaster;
  @Mock private VersionCookieUtils mockCookieUtils;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ApplicantController(mockRouteMaster, mockCookieUtils);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showApplicant_ShouldDisplayTheApplicantTemplate() throws Exception {

    ApplicantForm formRequest = ApplicantForm.builder().build();

    RadioOptionsGroup applicantOptions = controller.getApplicantOptions();

    mockMvc
        .perform(get("/applicant").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attribute("applicantOptions", applicantOptions))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void submitApplicant_ShouldStoreApplicantFormIntoSessionAndDisplayNextPageInTheJourney()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(ApplicantForm.class), any()))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/applicant")
                .param("applicantType", "YOURSELF")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submitApplicant_shouldDisplayValidationMessageWhenNoApplicantTypeIsSelected()
      throws Exception {
    mockMvc
        .perform(post("/applicant").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "applicantType", "NotNull"));
  }
}
