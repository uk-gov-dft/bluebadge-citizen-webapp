package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOption;
import uk.gov.dft.bluebadge.webapp.citizen.model.RadioOptionsGroup;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;

public class GenderControllerTest {

  private MockMvc mockMvc;
  private GenderController controller;

  @Mock private RouteMaster mockRouteMaster;
  private Journey journey;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new GenderController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    journey = new Journey();
    journey.setApplicantForm(
        ApplicantForm.builder().applicantType(ApplicantType.YOURSELF.name()).build());

    CompoundDate date = new CompoundDate();
    date.setDay("03");
    date.setMonth("12");
    date.setYear("1988");

    journey.setDateOfBirthForm(DateOfBirthForm.builder().dateOfBirth(date).build());
  }

  @Test
  public void show_ShouldDisplayGenderTemplate() throws Exception {
    GenderForm formRequest = GenderForm.builder().build();

    mockMvc
        .perform(get("/gender").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("gender"))
        .andExpect(model().attribute("formRequest", formRequest))
        .andExpect(model().attribute("options", Matchers.notNullValue()));
  }

  @Test
  public void show_ShouldDisplayYouGenderTerm() throws Exception {

    journey.getApplicantForm().setApplicantType(ApplicantType.YOURSELF.toString());

    MvcResult mvcResult =
        mockMvc
            .perform(get("/gender").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("gender"))
            .andReturn();
    Map<String, Object> model = mvcResult.getModelAndView().getModel();
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

    journey.getApplicantForm().setApplicantType(ApplicantType.SOMEONE_ELSE.toString());
    journey.setApplicantForm(journey.getApplicantForm());

    MvcResult mvcResult =
        mockMvc
            .perform(get("/gender").sessionAttr("JOURNEY", journey))
            .andExpect(status().isOk())
            .andExpect(view().name("gender"))
            .andReturn();

    Map<String, Object> model = mvcResult.getModelAndView().getModel();
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
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/gender"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submit_givenMaleOption_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(GenderForm.class), any(Journey.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/gender").param("gender", "MALE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenFemaleOption_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(GenderForm.class), any(Journey.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/gender").param("gender", "FEMALE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_givenUnspecifiedOption_thenShouldDisplayRedirectToSuccess() throws Exception {
    when(mockRouteMaster.redirectToOnSuccess(any(GenderForm.class), any(Journey.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(post("/gender").param("gender", "UNSPECIFIE").sessionAttr("JOURNEY", journey))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));
  }

  @Test
  public void submit_whenMissingGenderAnswer_ThenShouldHaveValidationError() throws Exception {
    mockMvc
        .perform(post("/gender").sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("gender"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "gender", "NotNull"));
  }
}
