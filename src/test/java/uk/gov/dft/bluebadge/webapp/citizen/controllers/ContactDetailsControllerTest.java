package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;

public class ContactDetailsControllerTest {

  public static final String URL_CONTACT_DETAILS = "/contact-details";
  public static final String VIEW_CONTACT_DETAILS = "contact-details";
  private MockMvc mockMvc;
  private ContactDetailsController controller;
  private Journey journey;

  @Mock private RouteMaster mockRouteMaster;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new ContactDetailsController(mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");
    when(mockRouteMaster.redirectToOnSuccess(any(DateOfBirthForm.class)))
        .thenReturn("redirect:/testSuccess");
  }

  @Test
  public void show_ShouldTemplate() throws Exception {

    // A pre-set up journey
    Journey journey = JourneyFixture.getDefaultJourney();

    mockMvc
        .perform(get(URL_CONTACT_DETAILS).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("contact-details"))
        .andExpect(model().attribute("formRequest", journey.getContactDetailsForm()));
  }

  @Test
  public void show_given3rdPartyApply_ShouldValidateFullName() throws Exception {

    // Set to applying for someone else
    Journey journey = JourneyFixture.getDefaultJourney();
    ApplicantForm formForStep = (ApplicantForm)journey.getFormForStep(StepDefinition.APPLICANT_TYPE);
    formForStep.setApplicantType(ApplicantType.SOMEONE_ELSE.toString());

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - invalid
                .param("primaryPhoneNumber", "01270161666")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(
            model()
                .attributeHasFieldErrorCode("formRequest", "fullName", "Invalid.contact.fullName"));
  }

  @Test
  public void show_givenYouApply_ShouldNotValidateFullName() throws Exception {

    // Set to applying for someone else
    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270161666")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(model().attributeDoesNotExist("fullName"));
  }

  @Test
  public void submit_givenYouApply_EmptyPrimaryContactNumber() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "primaryPhoneNumber", "NotBlank"));
  }

  @Test
  public void submit_givenYouApply_InvalidPrimaryContactNumber() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "afaf123")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "primaryPhoneNumber", "Pattern"));
  }

  @Test
  public void submit_givenYouApply_InvalidSecondaryContactNumber() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("secondaryPhoneNumber", "afaf123")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(
            model().attributeHasFieldErrorCode("formRequest", "secondaryPhoneNumber", "Pattern"));
  }

  @Test
  public void submit_givenYouApply_InvalidEmailAddress() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("emailAddress", "abc.com")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "emailAddress", "Pattern"));
  }

  @Test
  public void submit_givenYouApply_ValidEmailAddress() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("emailAddress", "a@bc.com")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAILS))
        .andExpect(model().attributeDoesNotExist("emailAddress"));
  }

  private void givenDefaultJourney() {
    // Set to applying for someone else
    journey = JourneyFixture.getDefaultJourney();
    ((ApplicantForm) journey.getFormForStep(StepDefinition.APPLICANT_TYPE)).setApplicantType(ApplicantType.YOURSELF.toString());
  }
}
