package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

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
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeCount;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasGlobalErrorCode;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney.SAVE_AND_RETURN_JOURNEY_KEY;
import static uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys.CODE;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.EnterCodeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoPostcodeException;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

public class EnterCodeControllerTest {

  private static final String ERROR_URL = Mappings.URL_ENTER_CODE + "#error";
  private MockMvc mockMvc;

  @Mock private CryptoService mockCryptoService;
  @Mock private RedisService mockRedisService;
  private SaveAndReturnJourney journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    journey = new SaveAndReturnJourney();
    EnterCodeController controller = new EnterCodeController(mockCryptoService, mockRedisService);
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
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("email").build());
    mockMvc
        .perform(get(Mappings.URL_ENTER_CODE).sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(model().attribute(EnterCodeController.EMAIL_MODEL_KEY, "email"))
        .andExpect(view().name(EnterCodeController.TEMPLATE));
  }

  @Test
  @SneakyThrows
  public void show_noEmail() {
    // Given
    // Email form not filled in
    journey.setSaveAndReturnForm(null);
    mockMvc
        .perform(get(Mappings.URL_ENTER_CODE).sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_RETURN_TO_APPLICATION));
  }

  @Test
  @SneakyThrows
  public void submit_ok() {
    // Given a valid submission
    journey.setSaveAndReturnForm(
        SaveAndReturnForm.builder().emailAddress("emailAddress@a.b").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress@a.b")).thenReturn("encrypted");
    when(mockRedisService.get(CODE, "emailAddress@a.b")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encrypted", "wv164aw");
  }

  @Test
  @SneakyThrows
  public void submit_bindingErrorsNulls() {
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("").postcode("").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("bindingErrors");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("code", "Pattern"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("postcode", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(2))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_bindingErrorsPatterns() {
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("12T4").postcode("A").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("bindingErrors");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("code", "Pattern"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("postcode", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(2))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_noStoredApplication() {

    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedNoApp");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedNoApp", "wv164aw");

    // Now tweak for specific fail
    when(mockRedisService.exists(JOURNEY, "emailAddress")).thenReturn(false);

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasGlobalErrorCode("enter.code.no.applications"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_throttleFail() {
    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedThrottle");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedThrottle", "wv164aw");

    // Now tweak for specific fail
    when(mockRedisService.throttleExceeded(any())).thenReturn(true);

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasGlobalErrorCode("enter.code.no.applications"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_codesDontMatch() {
    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedcodemismatch");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedcodemismatch", "wv164aw");

    // Now tweak for specific fail
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1235");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasGlobalErrorCode("enter.code.no.applications"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_noCodeInRedis() {
    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedcodemismatch");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedcodemismatch", "wv164aw");

    // Now tweak for specific fail
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn(null);

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasGlobalErrorCode("enter.code.no.applications"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test
  @SneakyThrows
  public void submit_postcodeDoesNotMatch() {
    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedcodemismatch");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedcodemismatch", "wv164aw");

    // Now tweak for specific fail
    when(mockCryptoService.decryptJourney(any(), any()))
        .thenThrow(new CryptoPostcodeException("", ""));

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(formRequestFlashAttributeHasGlobalErrorCode("enter.code.no.applications"))
        .andExpect(redirectedUrl(ERROR_URL));
  }

  @Test(expected = Exception.class)
  @SneakyThrows
  public void submit_wrongAppVersion() {
    // Check valid path first
    journey.setSaveAndReturnForm(SaveAndReturnForm.builder().emailAddress("emailAddress").build());
    EnterCodeForm form = EnterCodeForm.builder().code("1234").postcode("wv164aw").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emailAddress")).thenReturn("encryptedcodemismatch");
    when(mockRedisService.get(CODE, "emailAddress")).thenReturn("1234");

    mockMvc
        .perform(
            post(Mappings.URL_ENTER_CODE)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCryptoService, times(1)).decryptJourney("encryptedcodemismatch", "wv164aw");

    // Now tweak for specific fail
    when(mockCryptoService.decryptJourney(any(), any()))
        .thenThrow(new CryptoVersionException("", ""));

    // Should get unchecked exception
    mockMvc.perform(
        post(Mappings.URL_ENTER_CODE)
            .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
            .params(FormObjectToParamMapper.convert(form)));
  }
}
