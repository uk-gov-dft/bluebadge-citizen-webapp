package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

public class DeclarationSubmitControllerTest {

  private MockMvc mockMvc;
  private DeclarationSubmitController controller;

  @Mock ApplicationManagementService appService;
  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new DeclarationSubmitController(appService, mockRouteMaster);
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

    when(mockRouteMaster.redirectToOnSuccess(controller)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "true"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    verify(appService, times(1)).create(any());
  }

  @Test
  public void submitDeclaration_shouldSendFormDataWithinApplication_WhenDeclarationIsAgreed()
      throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(controller)).thenReturn("redirect:/testSuccess");

    Journey journey = new Journey();
    HealthConditionsForm healthConditionsForm =
        HealthConditionsForm.builder().descriptionOfConditions("test description").build();
    journey.setHealthConditionsForm(healthConditionsForm);

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
    verify(appService, times(1)).create(captor.capture());

    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getEligibility()).isNotNull();
    assertThat(captor.getValue().getEligibility().getDescriptionOfConditions())
        .isEqualTo("test description");
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
}
