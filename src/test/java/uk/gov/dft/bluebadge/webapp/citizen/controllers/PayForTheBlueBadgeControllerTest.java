package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PayForTheBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.PaymentService;

public class PayForTheBlueBadgeControllerTest {

  private MockMvc mockMvc;
  private Journey journey;

  //private static final String SUCCESS_URL = Mappings.URL_NINO;
  @Mock private PaymentService paymentServiceMock;

  @Mock private MessageSource messageSourceMock;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    PayForTheBadgeController controller =
        new PayForTheBadgeController(paymentServiceMock, new RouteMaster(), messageSourceMock);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey =
        JourneyFixture.getDefaultJourneyToStep(
            StepDefinition.PAY_FOR_THE_BADGE, EligibilityCodeField.DLA);
  }

  @Test
  public void show_ShouldDisplayPayForTheBlueBadgeTemplate() throws Exception {
    PayForTheBadgeForm formRequest = PayForTheBadgeForm.builder().build();

    mockMvc
        .perform(get("/pay-for-the-badge").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("pay-for-the-badge"))
        .andExpect(model().attribute("formRequest", formRequest));
    //        .andExpect(model().attribute("options", Matchers.notNullValue()));
  }
  /*
  @Test
  public void show_ShouldDisplayYouGenderTerm() throws Exception {

    Journey youJourney = new JourneyBuilder().forYou().toStep(StepDefinition.GENDER).build();

    MvcResult mvcResult =
        mockMvc
            .perform(get("/gender").sessionAttr("JOURNEY", youJourney))
            .andExpect(status().isOk())
            .andExpect(view().name("gender"))
            .andReturn();
    Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
    RadioOptionsGroup radioOptionsGroup = (RadioOptionsGroup) model.get("options");
    Optional<RadioOption> foundRadioOption =
        radioOptionsGroup
            .getOptions()
            .stream()
            .filter(option -> option.getMessageKey().equals("you.adult.radio.label.unspecified"))
            .findFirst();

    assertTrue(foundRadioOption.isPresent());
  }

  @Test
  public void show_ShouldDisplayTheyGenderTerm() throws Exception {

    Journey someoneElseJourney =
        new JourneyBuilder().forSomeOneElse().toStep(StepDefinition.GENDER).build();

    MvcResult mvcResult =
        mockMvc
            .perform(get("/gender").sessionAttr("JOURNEY", someoneElseJourney))
            .andExpect(status().isOk())
            .andExpect(view().name("gender"))
            .andReturn();

    Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
    RadioOptionsGroup radioOptionsGroup = (RadioOptionsGroup) model.get("options");
    Optional<RadioOption> foundRadioOption =
        radioOptionsGroup
            .getOptions()
            .stream()
            .filter(option -> option.getMessageKey().equals("oth.adult.radio.label.unspecified"))
            .findFirst();

    assertTrue(foundRadioOption.isPresent());
  }

  @Test
  public void show_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    mockMvc
        .perform(get("/gender"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  public void submit_givenMaleOption_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(post("/gender").param("gender", "MALE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenFemaleOption_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(post("/gender").param("gender", "FEMALE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_givenUnspecifiedOption_thenShouldDisplayRedirectToSuccess() throws Exception {

    mockMvc
        .perform(post("/gender").param("gender", "UNSPECIFIE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl(SUCCESS_URL));
  }

  @Test
  public void submit_whenMissingGenderAnswer_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post("/gender").sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_GENDER + RouteMaster.ERROR_SUFFIX))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode("gender", "NotNull"));
  }*/
}
