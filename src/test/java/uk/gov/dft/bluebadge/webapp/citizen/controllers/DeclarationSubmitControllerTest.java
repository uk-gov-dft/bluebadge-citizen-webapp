package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static org.assertj.core.api.Assertions.assertThat;
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
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.webapp.citizen.StandaloneMvcTestViewResolver;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HealthcareProfessional;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

public class DeclarationSubmitControllerTest {

  private MockMvc mockMvc;
  private DeclarationSubmitController controller;

  @Mock ApplicationManagementService appService;
  @Mock private RouteMaster mockRouteMaster;
  @Mock Journey mockJourney;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    controller = new DeclarationSubmitController(appService, mockRouteMaster);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setViewResolvers(new StandaloneMvcTestViewResolver())
            .build();
  }

  @Test
  public void showDeclaration_ShouldDisplayDeclarationTemplate() throws Exception {
    when(mockJourney.isValidState(any())).thenReturn(true);

    DeclarationForm formRequest = DeclarationForm.builder().build();

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration").sessionAttr("JOURNEY", mockJourney))
        .andExpect(status().isOk())
        .andExpect(view().name("application-end/declaration"))
        .andExpect(model().attribute("formRequest", formRequest));
  }

  @Test
  public void showDeclaration_givenNoSession_ShouldRedirectBackToStart() throws Exception {

    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void showDeclaration_givenInvalidState_ShouldRedirectBackToStart() throws Exception {

    when(mockJourney.isValidState(any())).thenReturn(false);
    when(mockRouteMaster.backToCompletedPrevious()).thenReturn("redirect:/backToStart");

    mockMvc
        .perform(get("/apply-for-a-blue-badge/declaration"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/backToStart"));
  }

  @Test
  public void submitDeclaration_ShouldDisplayApplicationSubmittedTemplate_WhenDeclarationIsAgreed()
      throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(DeclarationForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    verify(appService, times(1)).create(any());
  }

  @Test
  public void submitDeclaration_shouldSendFormDataWithinApplication_WhenDeclarationIsAgreed()
      throws Exception {

    when(mockRouteMaster.redirectToOnSuccess(any(DeclarationForm.class)))
        .thenReturn("redirect:/testSuccess");

    mockMvc
        .perform(
            post("/apply-for-a-blue-badge/declaration")
                .param("agreed", "true")
                .sessionAttr("JOURNEY", JourneyFixture.getDefaultJourney()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/testSuccess"));

    ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
    verify(appService, times(1)).create(captor.capture());

    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getEligibility()).isNotNull();
    assertThat(captor.getValue().getEligibility().getDescriptionOfConditions())
        .isEqualTo("test description - Able to walk to: London - How long: 10 minutes");
  }

  @Test
  public void submitDeclaration_ShouldThrowValidationError_WhenDeclarationIsNotAgreed()
      throws Exception {
    mockMvc
        .perform(post("/apply-for-a-blue-badge/declaration").param("agreed", "false"))
        .andExpect(status().isOk())
        .andExpect(view().name("apply-for-a-blue-badge/declaration"))
        .andExpect(model().attributeHasFieldErrorCode("formRequest", "agreed", "AssertTrue"));
  }

  @ParameterizedTest
  @EnumSource(
    value = EligibilityCodeField.class,
    names = {"ARMS", "CHILDBULK", "CHILDVEHIC", "WALKD"}
  )
  public void dummyApplication_healthConditionsDescSet_whenParticularEligibility(
      EligibilityCodeField eligibilityCode) {

    Journey journey = JourneyFixture.getDefaultJourney();
    MainReasonForm mainReasonForm =
        MainReasonForm.builder().mainReasonOption(eligibilityCode).build();
    journey.setMainReasonForm(mainReasonForm);
    HealthConditionsForm healthConditionsForm =
        HealthConditionsForm.builder().descriptionOfConditions("Test description ABC").build();
    journey.setHealthConditionsForm(healthConditionsForm);
    if (WALKD == eligibilityCode) {
      MobilityAidAddForm aid = new MobilityAidAddForm();
      aid.setUsage("Usage");
      aid.setAidType(MobilityAidAddForm.AidType.WHEELCHAIR);
      aid.setHowProvidedCodeField(HowProvidedCodeField.PRESCRIBE);
      journey.setMobilityAidListForm(
          MobilityAidListForm.builder()
              .hasWalkingAid("yes")
              .mobilityAids(Lists.newArrayList(aid))
              .build());

      TreatmentAddForm treatment = new TreatmentAddForm();
      treatment.setTreatmentWhen("Treatment when");
      treatment.setTreatmentDescription("Treatment description");
      journey.setTreatmentListForm(
          TreatmentListForm.builder()
              .hasTreatment("yes")
              .treatments(Lists.newArrayList(treatment))
              .build());
    }
    if (EnumSet.of(WALKD, CHILDBULK, CHILDVEHIC).contains(eligibilityCode)) {
      HealthcareProfessionalAddForm healthcareProfessional = new HealthcareProfessionalAddForm();
      healthcareProfessional.setHealthcareProfessionalName("name");
      healthcareProfessional.setHealthcareProfessionalLocation("location");

      journey.setHealthcareProfessionalListForm(
          HealthcareProfessionalListForm.builder()
              .hasHealthcareProfessional("yes")
              .healthcareProfessionals(Lists.newArrayList(healthcareProfessional))
              .build());
    }
    Application application = controller.getDummyApplication(journey);

    assertThat(application).isNotNull();
    assertThat(application.getEligibility()).isNotNull();
    assertThat(application.getEligibility().getTypeCode()).isEqualTo(eligibilityCode);
    if (WALKD == eligibilityCode) {
      assertThat(application.getEligibility().getDescriptionOfConditions())
          .isEqualTo("Test description ABC - Able to walk to: London - How long: 10 minutes");
      assertThat(application.getEligibility().getWalkingDifficulty().getWalkingAids().size())
          .isEqualTo(1);
      assertThat(application.getEligibility().getWalkingDifficulty().getTreatments().size())
          .isEqualTo(1);
    } else {
      assertThat(application.getEligibility().getDescriptionOfConditions())
          .isEqualTo("Test description ABC");
    }
    if (EnumSet.of(WALKD, CHILDBULK, CHILDVEHIC).contains(eligibilityCode)) {
      HealthcareProfessional hp = new HealthcareProfessional().location("location").name("name");
      assertThat(application.getEligibility().getHealthcareProfessionals().size()).isEqualTo(1);
      assertThat(application.getEligibility().getHealthcareProfessionals()).contains(hp);
    } else {
      assertThat(application.getEligibility().getHealthcareProfessionals()).isNull();
    }
  }

  @ParameterizedTest
  @EnumSource(
    value = EligibilityCodeField.class,
    names = {"PIP", "DLA", "WPMS", "AFRFCS", "BLIND"}
  )
  public void dummyApplication_healthConditionsDescNotSet_whenParticularEligibility(
      EligibilityCodeField eligibilityCode) {
    Journey journey = JourneyFixture.getDefaultJourney();
    MainReasonForm mainReasonForm =
        MainReasonForm.builder().mainReasonOption(eligibilityCode).build();
    journey.setMainReasonForm(mainReasonForm);
    HealthConditionsForm healthConditionsForm =
        HealthConditionsForm.builder().descriptionOfConditions("Test description ABC").build();
    journey.setHealthConditionsForm(healthConditionsForm);

    Application application = controller.getDummyApplication(journey);

    assertThat(application).isNotNull();
    assertThat(application.getEligibility()).isNotNull();
    assertThat(application.getEligibility().getTypeCode()).isEqualTo(eligibilityCode);
    assertThat(application.getEligibility().getDescriptionOfConditions()).isNull();
    assertThat(application.getEligibility().getHealthcareProfessionals()).isNull();
  }

  @Test
  public void dummyApplication_givenWalkingEligibility_thenWalkingTimeSet() {
    Journey journey = JourneyFixture.getDefaultJourney();
    MainReasonForm mainReasonForm = MainReasonForm.builder().mainReasonOption(WALKD).build();
    journey.setMainReasonForm(mainReasonForm);
    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.MORETEN).build();
    journey.setWalkingTimeForm(walkingTimeForm);
    Application application = controller.getDummyApplication(journey);

    assertThat(application).isNotNull();
    assertThat(application.getEligibility()).isNotNull();
    assertThat(application.getEligibility().getTypeCode()).isEqualTo(WALKD);
    assertThat(application.getEligibility().getWalkingDifficulty()).isNotNull();
    assertThat(application.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode())
        .isEqualTo(WalkingLengthOfTimeCodeField.MORETEN);
    assertThat(application.getEligibility().getWalkingDifficulty().getWalkingSpeedCode()).isNull();
  }

  @Test
  public void dummyApplication_givenCantWalking_thenWalkingSpeedNotSet() {
    Journey journey = JourneyFixture.getDefaultJourney();
    MainReasonForm mainReasonForm = MainReasonForm.builder().mainReasonOption(WALKD).build();
    journey.setMainReasonForm(mainReasonForm);
    WalkingTimeForm walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(WalkingLengthOfTimeCodeField.CANTWALK).build();
    journey.setWalkingTimeForm(walkingTimeForm);
    Application application = controller.getDummyApplication(journey);

    assertThat(application).isNotNull();
    assertThat(application.getEligibility()).isNotNull();
    assertThat(application.getEligibility().getTypeCode()).isEqualTo(WALKD);
    assertThat(application.getEligibility().getWalkingDifficulty()).isNotNull();
    assertThat(application.getEligibility().getWalkingDifficulty().getWalkingLengthOfTimeCode())
        .isEqualTo(WalkingLengthOfTimeCodeField.CANTWALK);
    assertThat(application.getEligibility().getWalkingDifficulty().getWalkingSpeedCode()).isNull();
  }

  @Test
  void dummyApplication_defaultValues() {
    Journey journey = JourneyFixture.getDefaultJourney();

    journey.getApplicantNameForm().setFullName(null);
    journey.getGenderForm().setGender(null);
    journey.getYourIssuingAuthorityForm().setLocalAuthorityShortCode(null);

    // Check nulls come through
    Application application = controller.getDummyApplication(journey);
    assertThat(application.getParty().getPerson().getBadgeHolderName()).isNull();
    assertThat(application.getParty().getPerson().getGenderCode()).isNull();
    assertThat(application.getParty().getPerson().getNino()).isNull();
    assertThat(application.getLocalAuthorityCode()).isNull();

    // And forms there, but no value
    journey.setNinoForm(NinoForm.builder().build());
    application = controller.getDummyApplication(journey);
    assertThat(application.getParty().getPerson().getNino()).isNull();
    assertThat(application.getParty().getPerson().getBadgeHolderName()).isNull();

    // Check not nulls still work
    journey = JourneyFixture.getDefaultJourney();
    journey.setNinoForm(NinoForm.builder().nino("NS333333A").build());
    application = controller.getDummyApplication(journey);
    assertThat(application.getParty().getPerson().getBadgeHolderName()).isEqualTo("John Doe");
    assertThat(application.getParty().getPerson().getGenderCode())
        .isEqualTo(GenderCodeField.FEMALE);
    assertThat(application.getParty().getPerson().getNino()).isEqualTo("NS333333A");
    assertThat(application.getLocalAuthorityCode()).isEqualTo("ABERD");
  }
}
