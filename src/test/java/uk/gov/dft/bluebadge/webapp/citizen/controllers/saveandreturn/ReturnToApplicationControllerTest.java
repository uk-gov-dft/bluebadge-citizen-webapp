package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeCount;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney.SAVE_AND_RETURN_JOURNEY_KEY;
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
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveAndReturnJourney;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoService;
import uk.gov.dft.bluebadge.webapp.citizen.service.CryptoVersionException;
import uk.gov.dft.bluebadge.webapp.citizen.service.MessageService;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;

public class ReturnToApplicationControllerTest {

  private MockMvc mockMvc;

  private ReturnToApplicationController controller;
  @Mock private CryptoService mockCryptoService;
  @Mock private MessageService mockMessageService;
  @Mock private RedisService mockRedisService;
  @Mock private RedisSessionConfig mockRedisSessionConfig;
  private SaveAndReturnJourney journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    journey = new SaveAndReturnJourney();
    controller =
        new ReturnToApplicationController(
            mockCryptoService, mockRedisService, mockMessageService, mockRedisSessionConfig);
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
        .perform(
            get(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(view().name(ReturnToApplicationController.TEMPLATE));
  }

  @Test
  @SneakyThrows
  public void submit_journeyMissing() {
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("").build();

    mockMvc
      .perform(
        post(Mappings.URL_RETURN_TO_APPLICATION)
          .params(FormObjectToParamMapper.convert(form)))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl(Mappings.URL_RETURN_TO_APPLICATION + "#error"));
  }

  @Test
  @SneakyThrows
  public void submit_loadsApplicationFailBeanValidation() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(eq(JOURNEY), any())).thenReturn("encrypted5");

    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_RETURN_TO_APPLICATION + "#error"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("emailAddress", "Pattern"))
        .andExpect(formRequestFlashAttributeCount(1));
    verify(mockCryptoService, never()).checkEncryptedJourneyVersion("encrypted5");
  }

  @Test
  @SneakyThrows
  public void submit_loadsApplicationFailBeanValidationInvalidEmail() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("123").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(eq(JOURNEY), any())).thenReturn("encrypted5");

    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_RETURN_TO_APPLICATION + "#error"))
        .andExpect(formRequestFlashAttributeHasFieldErrorCode("emailAddress", "Pattern"));
    verify(mockCryptoService, never()).checkEncryptedJourneyVersion("encrypted5");
  }

  @Test
  @SneakyThrows
  public void submit_loadsApplication() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("emai@l").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emai@l")).thenReturn("encrypted");

    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ENTER_CODE));
    verify(mockCryptoService, times(1)).checkEncryptedJourneyVersion("encrypted");
  }

  @Test
  @SneakyThrows
  public void submit_loadsApplicationSwitchesVersion() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("emai@l").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emai@l")).thenReturn("encrypted3");
    doThrow(new CryptoVersionException("", ""))
        .when(mockCryptoService)
        .checkEncryptedJourneyVersion(any());
    when(mockRedisSessionConfig.getStoredJourneyVersionCookieName()).thenReturn("MyNewCookie");
    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ENTER_CODE))
        .andExpect(cookie().exists("MyNewCookie"));
    verify(mockCryptoService, times(1)).checkEncryptedJourneyVersion("encrypted3");
  }

  @Test
  @SneakyThrows
  public void submit_noApplication() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("emai@l").build();

    when(mockRedisService.exists(any(), any())).thenReturn(false);
    when(mockRedisService.throttleExceeded(any())).thenReturn(false);
    when(mockRedisService.get(JOURNEY, "emai@l")).thenReturn("encrypted1");

    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ENTER_CODE));
    // Does not bother checking version
    verify(mockCryptoService, never()).checkEncryptedJourneyVersion("encrypted1");
  }

  @Test
  @SneakyThrows
  public void submit_tooManyTries() {
    // Given a valid submission
    SaveAndReturnForm form = SaveAndReturnForm.builder().emailAddress("emai@l").build();

    when(mockRedisService.exists(any(), any())).thenReturn(true);
    when(mockRedisService.throttleExceeded(any())).thenReturn(true);
    when(mockRedisService.get(JOURNEY, "emai@l")).thenReturn("encrypted2");

    mockMvc
        .perform(
            post(Mappings.URL_RETURN_TO_APPLICATION)
                .sessionAttr(SAVE_AND_RETURN_JOURNEY_KEY, journey)
                .params(FormObjectToParamMapper.convert(form)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_ENTER_CODE));
    verify(mockCryptoService, never()).checkEncryptedJourneyVersion("encrypted2");
  }

  @Test
  public void check4digitcode() {
    String code = controller.generate4DigitCode();
    assertThat(code.length()).isEqualTo(4);
  }
}
