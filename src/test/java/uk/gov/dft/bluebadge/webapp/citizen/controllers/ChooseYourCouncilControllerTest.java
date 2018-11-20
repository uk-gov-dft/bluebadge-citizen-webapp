package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalCouncilRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class ChooseYourCouncilControllerTest {

  private MockMvc mockMvc;

  @Mock ReferenceDataService mockReferenceDataService;

  Journey journey;

  private LocalCouncilRefData localCouncilRefData1;
  private LocalCouncilRefData localCouncilRefData2;
  private List<ReferenceData> councils;
  private List<ReferenceData> activeCouncils;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ChooseYourCouncilController controller =
        new ChooseYourCouncilController(mockReferenceDataService, new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.CHOOSE_COUNCIL);

    LocalCouncilRefData.LocalCouncilMetaData localCouncilMetaData1 =
        new LocalCouncilRefData.LocalCouncilMetaData();
    localCouncilMetaData1.setIsActive(false);
    localCouncilMetaData1.setIssuingAuthorityShortCode("WARCC");
    localCouncilRefData1 = new LocalCouncilRefData();
    localCouncilRefData1.setDescription("Description");
    localCouncilRefData1.setDisplayOrder(1);
    localCouncilRefData1.setLocalCouncilMetaData(localCouncilMetaData1);

    LocalCouncilRefData.LocalCouncilMetaData localCouncilMetaData2 =
        new LocalCouncilRefData.LocalCouncilMetaData();
    localCouncilMetaData2.setIsActive(true);
    localCouncilMetaData2.setIssuingAuthorityShortCode("KENTCC");
    localCouncilRefData2 = new LocalCouncilRefData();
    localCouncilRefData2.setDescription("Description");
    localCouncilRefData2.setDisplayOrder(1);
    localCouncilRefData2.setLocalCouncilMetaData(localCouncilMetaData2);

    councils = Lists.newArrayList(localCouncilRefData1, localCouncilRefData2);
    activeCouncils = Lists.newArrayList(localCouncilRefData2);
  }

  @Test
  public void show_whenFirstVisit() throws Exception {
    when(mockReferenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL))
        .thenReturn(councils);
    ChooseYourCouncilForm formRequest = ChooseYourCouncilForm.builder().build();

    Journey shortJourney = JourneyFixture.getDefaultJourneyToStep(StepDefinition.APPLICANT_TYPE);
    mockMvc
        .perform(get(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", shortJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("councils", activeCouncils));
  }

  @Test
  public void show_whenRevisit() throws Exception {

    when(mockReferenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL))
        .thenReturn(councils);

    mockMvc
        .perform(get(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(model().attribute("formRequest", JourneyFixture.getChooseYourCouncilForm()))
        .andExpect(model().attribute("councils", activeCouncils));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(
            post(Mappings.URL_CHOOSE_YOUR_COUNCIL)
                .param("councilShortCode", "test test")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_YOUR_ISSUING_AUTHORITY));
  }

  @Test
  public void submit_whenBindingException_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_CHOOSE_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "councilShortCode", "NotBlank"));
  }
}
