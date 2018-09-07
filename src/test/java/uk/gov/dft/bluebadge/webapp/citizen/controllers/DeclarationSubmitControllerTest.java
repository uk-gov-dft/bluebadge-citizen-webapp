package uk.gov.dft.bluebadge.webapp.citizen.controllers;

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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

public class DeclarationSubmitControllerTest {

  private MockMvc mockMvc;
  private DeclarationSubmitController controller;

  @Mock ApplicationManagementService appService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new DeclarationSubmitController(appService);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showDeclaration_ShouldDisplayDeclarationTemplate() throws Exception {

    DeclarationForm formRequest = DeclarationForm.builder().build();

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/declaration"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void submitDeclaration_ShouldDisplayApplicationSubmittedTemplate_WhenDeclarationIsAgreed()
      throws Exception {

    DeclarationForm formRequest = DeclarationForm.builder().build();

    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/application-submitted"));
  }

  @Test
  public void submitDeclaration_ShouldThrowValidationError_WhenDeclarationIsNotAgreed()
      throws Exception {
    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "false"))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/declaration"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "agreed", "AssertTrue"));
  }

  @Test
  public void showApplicationSubmitted_ShouldDisplayApplicationSubmittedTemplate()
      throws Exception {

    mockMvc
        .perform(get("/application-submitted"))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/submitted"));
  }
}
