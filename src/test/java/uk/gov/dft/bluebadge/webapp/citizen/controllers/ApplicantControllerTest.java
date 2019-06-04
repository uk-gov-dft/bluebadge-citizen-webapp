package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.config.RedisSessionConfig;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.utilities.RedirectVersionCookieManager;

public class ApplicantControllerTest {

  private static final String APP_VERSION_COOKIE_NAME = "V_Cookie";
  private MockMvc mockMvc;
  private ApplicantController controller;
  @Mock private RouteMaster mockRouteMaster;
  @Spy private RedirectVersionCookieManager cookieManager;

  @Before
  public void setup() {
    RedisSessionConfig redisConfig = new RedisSessionConfig();
    redisConfig.setStoredJourneyVersionCookieName(APP_VERSION_COOKIE_NAME);
    cookieManager = new RedirectVersionCookieManager(redisConfig);
    MockitoAnnotations.initMocks(this);
    controller = new ApplicantController(mockRouteMaster, cookieManager);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void
      show_whenNewJourneyAndNoVersionCookie_thenShouldDisplayTheApplicantTemplateAndSetVersionCookie()
          throws Exception {

    ApplicantForm formRequest = ApplicantForm.builder().build();

    RadioOptionsGroup applicantOptions = controller.getApplicantOptions();

    mockMvc
        .perform(get("/applicant").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attribute("applicantOptions", applicantOptions))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(cookie().value(APP_VERSION_COOKIE_NAME, "0.55.0"))
        .andExpect(cookie().maxAge(APP_VERSION_COOKIE_NAME, Matchers.greaterThan(24 * 60 * 60)));

    verify(cookieManager).removeCookie(any());
  }

  @Test
  public void show_whenNewJourneyAndVersionCookieSetWithDiffValue_thenRedirectAndClearCookie()
      throws Exception {

    Journey journey = new Journey();

    mockMvc
        .perform(
            get("/applicant")
                .sessionAttr("JOURNEY", journey)
                .sessionAttr("SOMETHING_RANDOM", "should be cleared")
                .cookie(new Cookie(APP_VERSION_COOKIE_NAME, "999")))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/applicant"))
        .andExpect(cookie().value(APP_VERSION_COOKIE_NAME, ""))
        .andExpect(cookie().maxAge(APP_VERSION_COOKIE_NAME, 0))
        .andExpect(request().sessionAttribute("SOMETHING_RANDOM", Matchers.nullValue()));


    verify(cookieManager).removeCookie(any());
  }

  @Test
  public void show_whenNewJourneyAndVersionCookieSetWithSameValue_thenRedirectAndClearCookie()
      throws Exception {

    Journey journey = new Journey();

    mockMvc
        .perform(
            get("/applicant")
                .sessionAttr("JOURNEY", journey)
                .sessionAttr("SOMETHING_RANDOM", "should be cleared")
                .cookie(new Cookie(APP_VERSION_COOKIE_NAME, "0.55.0")))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/applicant"))
        .andExpect(cookie().value(APP_VERSION_COOKIE_NAME, ""))
        .andExpect(cookie().maxAge(APP_VERSION_COOKIE_NAME, 0))
        .andExpect(request().sessionAttribute("SOMETHING_RANDOM", Matchers.nullValue()));

    verify(cookieManager).removeCookie(any());
  }

  @Test
  public void
      show_whenExistingJourneyAndNoVersionCookie_thenShouldDisplayTheApplicantTemplateAndSetVersionCookie()
          throws Exception {

    ApplicantForm formRequest = ApplicantForm.builder().applicantType("whatever").build();

    RadioOptionsGroup applicantOptions = controller.getApplicantOptions();

    Journey journey = new Journey();
    journey.setFormForStep(formRequest);
    mockMvc
        .perform(get("/applicant").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attribute("applicantOptions", applicantOptions))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(cookie().value(APP_VERSION_COOKIE_NAME, "0.55.0"));

    verify(cookieManager).removeCookie(any());
  }

  @Test
  public void
      show_whenExistingJourneyAndVersionCookieSet_thenShouldDisplayTheApplicantTemplateAndDoNotChangeCookie()
          throws Exception {

    ApplicantForm formRequest = ApplicantForm.builder().applicantType("whatever").build();

    RadioOptionsGroup applicantOptions = controller.getApplicantOptions();

    Journey journey = new Journey();
    journey.setFormForStep(formRequest);
    mockMvc
        .perform(
            get("/applicant")
                .sessionAttr("JOURNEY", journey)
                .cookie(new Cookie(APP_VERSION_COOKIE_NAME, "999")))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attribute("applicantOptions", applicantOptions))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(cookie().value(APP_VERSION_COOKIE_NAME, "999"));

    verify(cookieManager).removeCookie(any());
  }

  @Test
  public void submitApplicant_ShouldStoreApplicantFormIntoSessionAndDisplayNextPageInTheJourney()
      throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(ApplicantForm.class), any()))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/applicant")
                .param("applicantType", "YOURSELF")
                .sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submitApplicant_shouldDisplayValidationMessageWhenNoApplicantTypeIsSelected()
      throws Exception {
    mockMvc
        .perform(post("/applicant").sessionAttr("JOURNEY", new Journey()))
        .andExpect(status().isOk())
        .andExpect(view().name("applicant"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "applicantType", "NotNull"));
  }
}
