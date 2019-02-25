package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;

public class ContactDetailsControllerTest {

  private static final String URL_CONTACT_DETAILS = "/contact-details";
  private MockMvc mockMvc;
  private Journey journey;

  private static final String SUCCESS_URL = Mappings.URL_PROVE_IDENTITY;
  private static final String ERROR_URL = Mappings.URL_CONTACT_DETAILS + RouteMaster.ERROR_SUFFIX;

  @Before
  public void setup() {
    ContactDetailsController controller = new ContactDetailsController(new RouteMaster());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void show_ShouldTemplate() throws Exception {

    // A pre-set up journey
    Journey journey = JourneyFixture.getDefaultJourney();

    mockMvc
        .perform(get(URL_CONTACT_DETAILS).sessionAttr("JOURNEY", journey))
        .andExpect(status().isOk())
        .andExpect(view().name("contact-details"))
        .andExpect(
            model()
                .attribute(
                    "formRequest",
                    (ContactDetailsForm) journey.getFormForStep(StepDefinition.CONTACT_DETAILS)));
  }

  @Test
  public void show_given3rdPartyApply_ShouldValidateFullName() throws Exception {

    // Set to applying for someone else
    Journey journey = JourneyFixture.getDefaultJourney();
    ApplicantForm formForStep = journey.getFormForStep(StepDefinition.APPLICANT_TYPE);
    formForStep.setApplicantType(ApplicantType.SOMEONE_ELSE.toString());

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - invalid
                .param("primaryPhoneNumber", "01270161666")
                .param("continueWithoutEmailAddress", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "fullName", "Invalid.contact.fullName"));
  }

  @Test
  public void show_givenYouApply_ShouldNotValidateFullName() throws Exception {

    journey = new JourneyBuilder().forYou().withEligibility(EligibilityCodeField.WPMS).build();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270161666")
                .param("continueWithoutEmailAddress", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(SUCCESS_URL))
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
                .param("continueWithoutEmailAddress", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "primaryPhoneNumber", "NotBlank"));
  }

  @Test
  public void submit_givenYouApply_InvalidPrimaryContactNumber() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "afaf123")
                .param("continueWithoutEmailAddress", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "primaryPhoneNumber", "Pattern"));
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
                .param("continueWithoutEmailAddress", "true")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "secondaryPhoneNumber", "Pattern"));
  }

  @Test
  public void submit_givenYouApply_InvalidEmailAddress() throws Exception {

    givenDefaultJourney();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("continueWithoutEmailAddress", "false")
                .param("emailAddress", "abc.com")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ERROR_URL))
        .andExpect(
            ControllerTestFixture.formRequestFlashAttributeHasFieldErrorCode(
                "emailAddress", "Invalid.emailAddress"));
  }

  @Test
  public void submit_givenYouApply_InvalidEmailAddressButContinueWithout() throws Exception {

    journey = new JourneyBuilder().forYou().withEligibility(EligibilityCodeField.WPMS).build();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("continueWithoutEmailAddress", "true")
                .param("emailAddress", "abc.com")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(SUCCESS_URL))
        .andExpect(model().attributeDoesNotExist("emailAddress"));
  }

  @Test
  public void submit_givenYouApply_ValidEmailAddress() throws Exception {

    journey = new JourneyBuilder().forYou().withEligibility(EligibilityCodeField.WPMS).build();

    mockMvc
        .perform(
            post(URL_CONTACT_DETAILS)
                .param("fullName", "") // empty - valid
                .param("primaryPhoneNumber", "01270646261")
                .param("continueWithoutEmailAddress", "false")
                .param("emailAddress", "a@bc.com")
                .sessionAttr("JOURNEY", journey))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(SUCCESS_URL))
        .andExpect(model().attributeDoesNotExist("emailAddress"));
  }

  private void givenDefaultJourney() {
    // Set to applying for someone else
    journey = JourneyFixture.getDefaultJourney();
    ((ApplicantForm) journey.getFormForStep(StepDefinition.APPLICANT_TYPE))
        .setApplicantType(ApplicantType.YOURSELF.toString());
  }
}
