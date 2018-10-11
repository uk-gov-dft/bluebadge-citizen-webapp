package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthcareProfessionalAddControllerTest
    extends ControllerTestFixture<HealthcareProfessionalAddController> {

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    super.setup(new HealthcareProfessionalAddController(mockRouteMaster));
    journey.setHealthcareProfessionalListForm(
        HealthcareProfessionalListForm.builder()
            .healthcareProfessionals(new ArrayList<>())
            .build());
    applyRoutmasterDefaultMocks(mockRouteMaster);
  }

  @Override
  String getTemplateName() {
    return "healthcare-professional-add";
  }

  @Override
  String getUrl() {
    return "/add-healthcare-professional";
  }

  @Test
  public void show_ShouldDisplayTemplate() throws Exception {
    super.show_ShouldDisplayTemplate();
  }

  @Test
  public void show_shouldRedirect_whenJourneyNotSetup() throws Exception {
    super.show_shouldRedirect_whenJourneyNotSetup();
  }

  @Test
  public void submit_showRedirectToNextStepInJourney() throws Exception {

    mockMvc
        .perform(
            post(getUrl())
                .param("healthcareProfessionalName", "Dr No")
                .param("healthcareProfessionalLocation", "Spooky Island")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/list-healthcare-professionals"));
  }

  @Test
  public void submit_whenBlankFormSubmitted_thenShouldRedirectToShowWithValidationErrors()
      throws Exception {

    HealthcareProfessionalAddForm form = HealthcareProfessionalAddForm.builder().id("1234").build();

    mockMvc
        .perform(
            post(getUrl())
                .param("id", "1234")
                .contentType("application/x-www-form-urlencoded")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/add-healthcare-professional"))
        .andExpect(flash().attribute("formRequest", form))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode("healthcareProfessionalName", "NotBlank"))
        .andExpect(
            formRequestFlashAttributeHasFieldErrorCode(
                "healthcareProfessionalLocation", "NotBlank"));
  }
}
