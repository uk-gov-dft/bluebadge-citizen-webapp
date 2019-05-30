package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeCount;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_ROOT;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn.SaveProgressController.FORM_REQUEST;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn.SaveProgressController.HIDE_POSTCODE_MODEL_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.JOURNEY;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.FormObjectToParamMapper;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveProgressForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.MessageService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

public class SaveProgressControllerTest {
  private static final String ERROR_URL = Mappings.URL_SAVE_PROGRESS + "#error";
  private MockMvc mockMvc;

  @Mock private CryptoService mockCryptoService;
  @Mock private RedisService mockRedisService;
  @Mock private MessageService mockMessageService;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    journey = new Journey();
    journey.setFormForStep(ApplicantForm.builder().applicantType("YOURSELF").build());
    SaveProgressController controller =
        new SaveProgressController(mockRedisService, mockCryptoService, mockMessageService);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  @SneakyThrows
  public void show() {
    // Given
    // Valid journey
    mockMvc
        .perform(get(Mappings.URL_SAVE_PROGRESS).sessionAttr(JOURNEY_SESSION_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(view().name(SaveProgressController.TEMPLATE))
        .andExpect(model().attributeExists(HIDE_POSTCODE_MODEL_KEY, "formRequest"))
        .andExpect(model().attribute(HIDE_POSTCODE_MODEL_KEY, false));
  }

  @Test
  @SneakyThrows
  public void show_emptyJourney() {
    mockMvc
      .perform(get(Mappings.URL_SAVE_PROGRESS).sessionAttr(JOURNEY_SESSION_KEY, new Journey()))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl(URL_ROOT));
  }

  @Test
  @SneakyThrows
  public void show_postcodeAndEmailPopulated() {
    // Given
    // Valid journey with postcode and email
    journey.setFormForStep(EnterAddressForm.builder().postcode("wv164aw").build());
    journey.setFormForStep(
        ContactDetailsForm.builder().emailAddress("bobbyboy@nowhere.com").build());

    mockMvc
        .perform(get(Mappings.URL_SAVE_PROGRESS).sessionAttr(JOURNEY_SESSION_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(view().name(SaveProgressController.TEMPLATE))
        .andExpect(model().attributeExists(HIDE_POSTCODE_MODEL_KEY, "formRequest"))
        .andExpect(model().attribute(HIDE_POSTCODE_MODEL_KEY, true))
        .andExpect(
            model()
                .attribute(
                    FORM_REQUEST,
                    SaveProgressForm.builder()
                        .emailAddress("bobbyboy@nowhere.com")
                        .postcode("wv164aw")
                        .build()));
  }

  @SneakyThrows
  @Test
  public void submit_ok() {
    // Given a valid submission
    SaveProgressForm form =
        SaveProgressForm.builder().emailAddress("submitOk@a.b").postcode("wv164aw").build();

    when(mockCryptoService.encryptJourney(any(), eq("wv164aw"))).thenReturn("encrypted");
    when(mockRedisService.getExpiryTimeFormatted(any(), any())).thenReturn("expiryTime");

    mockMvc
        .perform(
            post(Mappings.URL_SAVE_PROGRESS)
                .sessionAttr(JOURNEY_SESSION_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_PROGRESS_SAVED));

    verify(mockRedisService, times(1)).setAndExpireIfNew(JOURNEY, "submitOk@a.b", "encrypted");
    verify(mockMessageService, times(1)).sendApplicationSavedEmail("submitOk@a.b", "expiryTime");
  }

  @SneakyThrows
  @Test
  public void submit_emptyJourney() {
    // Given a valid submission
    SaveProgressForm form =
      SaveProgressForm.builder().emailAddress("submitOk@a.b").postcode("wv164aw").build();
    mockMvc
      .perform(
        post(Mappings.URL_SAVE_PROGRESS)
          .sessionAttr(JOURNEY_SESSION_KEY, new Journey())
          .params(FormObjectToParamMapper.convert(form)))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl(Mappings.URL_ROOT));
  }

  @Test
  @SneakyThrows
  public void submit_bindingErrorsNulls() {

    mockMvc
        .perform(
            post(Mappings.URL_SAVE_PROGRESS)
                .sessionAttr(JOURNEY_SESSION_KEY, journey)
                .params(
                    FormObjectToParamMapper.convert(
                        SaveProgressForm.builder().emailAddress("").postcode("").build())))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("emailAddress", "Pattern"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("postcode", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(2))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_bindingErrorsPatterns() {

    mockMvc
        .perform(
            post(Mappings.URL_SAVE_PROGRESS)
                .sessionAttr(JOURNEY_SESSION_KEY, journey)
                .params(
                    FormObjectToParamMapper.convert(
                        SaveProgressForm.builder().postcode("W").emailAddress("A").build())))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("emailAddress", "Pattern"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("postcode", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(2))
        .andExpect(redirectedUrl(ERROR_URL));
  }
}
