package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.CHOOSE_COUNCIL;

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
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalCouncilRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.ReferenceData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class ChooseYourCouncilControllerTest {

  private static final String COUNCIL_SHORT_CODE = "test test";

  private MockMvc mockMvc;

  @Mock ReferenceDataService mockReferenceDataService;

  Journey journey;

  private List<ReferenceData> councils;
  private LocalAuthorityRefData activeLA;
  private LocalAuthorityRefData inactiveLA;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ChooseYourCouncilController controller =
        new ChooseYourCouncilController(mockReferenceDataService, RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(CHOOSE_COUNCIL);

    LocalCouncilRefData.LocalCouncilMetaData localCouncilMetaData1 =
        new LocalCouncilRefData.LocalCouncilMetaData();
    localCouncilMetaData1.setIssuingAuthorityShortCode("WARCC");
    LocalCouncilRefData localCouncilRefData1 = new LocalCouncilRefData();
    localCouncilRefData1.setDescription("Description");
    localCouncilRefData1.setDisplayOrder(1);
    localCouncilRefData1.setLocalCouncilMetaData(localCouncilMetaData1);

    LocalCouncilRefData.LocalCouncilMetaData localCouncilMetaData2 =
        new LocalCouncilRefData.LocalCouncilMetaData();
    localCouncilMetaData2.setIssuingAuthorityShortCode("KENTCC");
    LocalCouncilRefData localCouncilRefData2 = new LocalCouncilRefData();
    localCouncilRefData2.setDescription("Description");
    localCouncilRefData2.setDisplayOrder(1);
    localCouncilRefData2.setLocalCouncilMetaData(localCouncilMetaData2);

    councils = Lists.newArrayList(localCouncilRefData1, localCouncilRefData2);

    LocalAuthorityRefData.LocalAuthorityMetaData meta =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    meta.setDifferentServiceSignpostUrl(null);
    activeLA = new LocalAuthorityRefData();
    activeLA.setLocalAuthorityMetaData(meta);

    meta = new LocalAuthorityRefData.LocalAuthorityMetaData();
    meta.setDifferentServiceSignpostUrl("/signpostUrl");
    inactiveLA = new LocalAuthorityRefData();
    inactiveLA.setLocalAuthorityMetaData(meta);
  }

  @Test
  public void show_whenFirstVisit() throws Exception {
    when(mockReferenceDataService.retrieveReferenceDataList(RefDataGroupEnum.COUNCIL))
        .thenReturn(councils);
    ChooseYourCouncilForm formRequest = ChooseYourCouncilForm.builder().build();

    Journey shortJourney = JourneyFixture.getDefaultJourneyToStep(StepDefinition.FIND_COUNCIL);
    mockMvc
        .perform(get(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", shortJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("choose-council"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attributeExists("councils"));
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
        .andExpect(model().attributeExists("councils"));
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockReferenceDataService.lookupLocalAuthorityFromCouncilCode(COUNCIL_SHORT_CODE))
        .thenReturn(activeLA);

    mockMvc
        .perform(
            post(Mappings.URL_CHOOSE_YOUR_COUNCIL)
                .param("councilShortCode", COUNCIL_SHORT_CODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_YOUR_ISSUING_AUTHORITY));
  }

  @Test
  public void submit_givenInactiveLA_thenShouldDisplayRedirectToSignpost() throws Exception {
    when(mockReferenceDataService.lookupLocalAuthorityFromCouncilCode(COUNCIL_SHORT_CODE))
        .thenReturn(inactiveLA);

    mockMvc
        .perform(
            post(Mappings.URL_CHOOSE_YOUR_COUNCIL)
                .param("councilShortCode", COUNCIL_SHORT_CODE)
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_DIFFERENT_SERVICE_SIGNPOST));
  }

  @Test
  public void submit_whenBindingException_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post(Mappings.URL_CHOOSE_YOUR_COUNCIL).sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_CHOOSE_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "councilShortCode", "NotBlank"));
  }
}
