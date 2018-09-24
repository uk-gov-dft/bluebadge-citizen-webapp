package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class ChooseYourCouncilControllerTest {

  private MockMvc mockMvc;
  private ChooseYourCouncilController controller;

  @Mock ReferenceDataService mockReferenceDataService;

  @Mock Journey mockJourney;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ChooseYourCouncilController(mockReferenceDataService, mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_whenFirstvisit() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);
    when(mockReferenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL))
        .thenReturn(Lists.newArrayList(new ReferenceData()));
    ChooseYourCouncilForm formRequest = ChooseYourCouncilForm.builder().build();

    mockMvc
        .perform(get(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attributeExists("councils"));
  }

  @Test
  public void show_whenRevisit() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);
    ChooseYourCouncilForm formRequest =
        ChooseYourCouncilForm.builder().councilShortCode("ABC").build();
    when(mockJourney.getChooseYourCouncilForm()).thenReturn(formRequest);
    when(mockReferenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL))
        .thenReturn(Lists.newArrayList(new ReferenceData()));

    mockMvc
        .perform(get(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attributeExists("councils"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(ChooseYourCouncilForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post(Mappings.URL_CHOOSE_YOUR_COUNCIL)
                .param("councilShortCode", "test test")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenBindingException_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "councilShortCode", "NotBlank"));
  }
}
