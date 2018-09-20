package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class YourIssuingAuthorityControllerTest {

  private MockMvc mockMvc;
  private YourIssuingAuthorityController controller;
  private LocalAuthorityRefData laRefData = new LocalAuthorityRefData();
  @Mock ReferenceDataService mockReferenceDataService;

  @Mock Journey mockJourney;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new YourIssuingAuthorityController(mockRouteMaster, mockReferenceDataService);
    when(mockJourney.getChooseYourCouncilForm())
        .thenReturn(ChooseYourCouncilForm.builder().councilShortCode("TTT").build());
    when(mockReferenceDataService.lookupLaForLcCode("TTT")).thenReturn(laRefData);
    laRefData.setShortCode("HHH");
    laRefData.setDescription("An LA");
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_whenFirstvisit() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);

    YourIssuingAuthorityForm formRequest =
        YourIssuingAuthorityForm.builder()
            .localAuthorityShortCode("HHH")
            .localAuthorityDescription("An LA")
            .build();

    mockMvc
        .perform(get(Mappings.URL_YOUR_ISSUING_AUTHORITY).sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("issuing-authority"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(controller)).thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post(Mappings.URL_YOUR_ISSUING_AUTHORITY).sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }
}
