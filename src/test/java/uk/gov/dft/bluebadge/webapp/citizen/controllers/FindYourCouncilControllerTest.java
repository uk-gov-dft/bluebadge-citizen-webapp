package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.BadRequestException;
import uk.gov.dft.bluebadge.webapp.citizen.client.common.NotFoundException;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.RouteMasterFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

public class FindYourCouncilControllerTest {

  public static final String POSTCODE = "M41FS";
  public static final String INVALID_POSTCODE = "aa1";
  private MockMvc mockMvc;

  @Mock ReferenceDataService mockReferenceDataService;

  Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    FindYourCouncilController controller =
        new FindYourCouncilController(mockReferenceDataService, RouteMasterFixture.routeMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.FIND_COUNCIL);
  }

  @Test
  public void show_whenFirstVisit() throws Exception {
    FindYourCouncilForm formRequest = FindYourCouncilForm.builder().build();

    Journey shortJourney = JourneyFixture.getDefaultJourneyToStep(StepDefinition.APPLICANT_TYPE);
    mockMvc
        .perform(get(Mappings.URL_FIND_YOUR_COUNCIL).sessionAttr("JOURNEY", shortJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("find-council"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_whenRevisit() throws Exception {
    FindYourCouncilForm formRequest = JourneyFixture.getFindYourCouncilForm();

    mockMvc
        .perform(get(Mappings.URL_FIND_YOUR_COUNCIL).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("find-council"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void show_whenInvalidState() throws Exception {
    FindYourCouncilForm formRequest = JourneyFixture.getFindYourCouncilForm();

    Journey emptyJourney = new Journey();

    mockMvc
        .perform(get(Mappings.URL_FIND_YOUR_COUNCIL).sessionAttr("JOURNEY", emptyJourney))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  @SneakyThrows
  public void formByPass_shouldRedirectToChooseYourCouncil() {
    mockMvc
        .perform(get(Mappings.URL_FIND_YOUR_COUNCIL_BYPASS).sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_CHOOSE_YOUR_COUNCIL));
    FindYourCouncilForm formRequest = journey.getFormForStep(StepDefinition.FIND_COUNCIL);
    FindYourCouncilForm expectedFormRequest = FindYourCouncilForm.builder().build();
    assertThat(formRequest).isEqualTo(expectedFormRequest);
  }

  @Test
  public void submit_givenValidForm_thenShouldDisplayRedirectToSuccess() throws Exception {
    LocalAuthorityRefData la = new LocalAuthorityRefData();
    la.setShortCode("MANC");
    la.setDescription("Manchester council");
    when(mockReferenceDataService.retrieveLAByPostcode(POSTCODE)).thenReturn(la);

    mockMvc
        .perform(
            post(Mappings.URL_FIND_YOUR_COUNCIL)
                .sessionAttr("JOURNEY", journey)
                .param("postcode", POSTCODE))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_YOUR_ISSUING_AUTHORITY));

    FindYourCouncilForm formRequest = journey.getFormForStep(StepDefinition.FIND_COUNCIL);
    FindYourCouncilForm expectFormRequest =
        FindYourCouncilForm.builder().postcode(POSTCODE).build();
    assertThat(formRequest).isEqualTo(expectFormRequest);
  }

  @Test
  public void submit_shouldRedirectToFindCouncilPageAndDisplayErrors_whenPostcodeIsNull()
      throws Exception {
    mockMvc
        .perform(
            post(Mappings.URL_FIND_YOUR_COUNCIL)
                .sessionAttr("JOURNEY", journey)
                .param("postcode", ""))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_FIND_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "Pattern"));
  }

  @Test
  public void submit_shouldRedirectToFindCouncilPageAndDisplayErrors_whenPostcodeIsInvalid()
      throws Exception {
    mockMvc
        .perform(
            post(Mappings.URL_FIND_YOUR_COUNCIL)
                .sessionAttr("JOURNEY", journey)
                .param("postcode", INVALID_POSTCODE))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_FIND_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "Pattern"));
  }

  @Test
  public void submit_shouldRedirectToFindCouncilPage_whenLaIsNotFound() throws Exception {
    CommonResponse commonResponse = new CommonResponse();
    when(mockReferenceDataService.retrieveLAByPostcode(POSTCODE))
        .thenThrow(new NotFoundException(commonResponse));

    mockMvc
        .perform(
            post(Mappings.URL_FIND_YOUR_COUNCIL)
                .sessionAttr("JOURNEY", journey)
                .param("postcode", POSTCODE))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_FIND_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "NotFound.findYourcouncil.postcode"));
    ;
  }

  @Test
  public void submit_shouldRedirectToFindCouncilPage_whenThereIsAnUnexpectedException()
      throws Exception {
    CommonResponse commonResponse = new CommonResponse();
    when(mockReferenceDataService.retrieveLAByPostcode(POSTCODE))
        .thenThrow(new BadRequestException(commonResponse));

    mockMvc
        .perform(
            post(Mappings.URL_FIND_YOUR_COUNCIL)
                .sessionAttr("JOURNEY", journey)
                .param("postcode", POSTCODE))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_FIND_YOUR_COUNCIL + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "postcode", "ServerError.findYourcouncil.postcode"));
    ;
  }
}
