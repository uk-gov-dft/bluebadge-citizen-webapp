package uk.gov.dft.bluebadge.webapp.citizen.controllers.saveandreturn;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.saveandreturn.SaveProgressForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisKeys;
import uk.gov.dft.bluebadge.webapp.citizen.service.RedisService;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

public class ProgressSavedControllerTest {
  private MockMvc mockMvc;

  @Mock private RedisService mockRedisService;
  @Mock private RedirectVersionCookieManager mockCookieManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new ProgressSavedController(mockRedisService, mockCookieManager))
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  @SneakyThrows
  public void show_days() {
    String emailAddress = "email1";
    Journey journey = new Journey();
    journey.setSaveProgressForm(SaveProgressForm.builder().emailAddress(emailAddress).build());
    when(mockRedisService.getDaysToExpiryRoundedUp(RedisKeys.JOURNEY, emailAddress)).thenReturn(4L);
    when(mockRedisService.getHoursToExpiry(RedisKeys.JOURNEY, emailAddress)).thenReturn(24L);

    mockMvc
        .perform(get(Mappings.URL_PROGRESS_SAVED).sessionAttr(JOURNEY_SESSION_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(view().name(ProgressSavedController.TEMPLATE))
        .andExpect(model().attribute(ProgressSavedController.TIME_TO_EXPIRY_MODEL_KEY, 4L))
        .andExpect(
            model()
                .attribute(
                    ProgressSavedController.TIME_TO_EXPIRY_UNITS_MODEL_KEY,
                    ProgressSavedController.UNITS_DAYS));
    verify(mockRedisService, never()).getHoursToExpiry(RedisKeys.JOURNEY, emailAddress);
    verify(mockCookieManager).removeCookie(any());
  }

  @Test
  @SneakyThrows
  public void show_hours() {
    String emailAddress = "email2";
    Journey journey = new Journey();
    journey.setSaveProgressForm(SaveProgressForm.builder().emailAddress(emailAddress).build());
    when(mockRedisService.getDaysToExpiryRoundedUp(RedisKeys.JOURNEY, emailAddress)).thenReturn(0L);
    when(mockRedisService.getHoursToExpiry(RedisKeys.JOURNEY, emailAddress)).thenReturn(24L);

    mockMvc
        .perform(get(Mappings.URL_PROGRESS_SAVED).sessionAttr(JOURNEY_SESSION_KEY, journey))
        .andExpect(status().isOk())
        .andExpect(view().name(ProgressSavedController.TEMPLATE))
        .andExpect(model().attribute(ProgressSavedController.TIME_TO_EXPIRY_MODEL_KEY, 24L))
        .andExpect(
            model()
                .attribute(
                    ProgressSavedController.TIME_TO_EXPIRY_UNITS_MODEL_KEY,
                    ProgressSavedController.UNITS_HOURS));
    verify(mockRedisService).getHoursToExpiry(RedisKeys.JOURNEY, emailAddress);
    verify(mockCookieManager).removeCookie(any());
  }

  @Test
  @SneakyThrows
  public void show_noPreviousPageVisited() {

    mockMvc
        .perform(get(Mappings.URL_PROGRESS_SAVED).sessionAttr(JOURNEY_SESSION_KEY, new Journey()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(Mappings.URL_TASK_LIST));
    verify(mockCookieManager, never()).removeCookie(any());
  }
}
