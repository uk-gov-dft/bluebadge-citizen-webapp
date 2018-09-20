package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Contact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Party;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.PartyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingSpeedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

@Controller
@RequestMapping(Mappings.URL_DECLARATIONS)
public class DeclarationSubmitController implements StepController {

  private static final String TEMPLATE_DECLARATION = "application-end/declaration";

  private final ApplicationManagementService appService;
  private final RouteMaster routeMaster;

  public DeclarationSubmitController(
      ApplicationManagementService appService, RouteMaster routeMaster) {
    this.appService = appService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String showDeclaration(Model model) {

    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", DeclarationForm.builder().build());
    }

    return TEMPLATE_DECLARATION;
  }

  @PostMapping
  public String submitDeclaration(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") DeclarationForm formRequest,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, formRequest, bindingResult, attr);
    }

    appService.create(getDummyApplication(journey));

    return routeMaster.redirectToOnSuccess(this);
  }

  private Application getDummyApplication(Journey journey) {
    ApplicantNameForm applicantNameForm = journey.getApplicantNameForm();
    HealthConditionsForm healthConditionsForm = journey.getHealthConditionsForm();
    YourIssuingAuthorityForm yourIssuingAuthorityForm = journey.getYourIssuingAuthorityForm();
    String la =
        yourIssuingAuthorityForm == null
            ? "ABERD"
            : yourIssuingAuthorityForm.getLocalAuthorityShortCode();
    String condDesc =
        healthConditionsForm == null
            ? "Dummy condition"
            : healthConditionsForm.getDescriptionOfConditions();

    String fullName = applicantNameForm == null ? "John Doe" : applicantNameForm.getFullName();
    String birthName =
        applicantNameForm == null ? "John Doe Birth" : applicantNameForm.getBirthName();

    Party party =
        new Party()
            .typeCode(PartyTypeCodeField.PERSON)
            .contact(
                new Contact()
                    .buildingStreet("65 Basil Chambers")
                    .line2("Northern Quarter")
                    .townCity("Manchester")
                    .postCode("SK6 8GH")
                    .primaryPhoneNumber("016111234567")
                    .secondaryPhoneNumber("079707777111")
                    .emailAddress("nobody@thisisatestabc.com"))
            .person(
                new Person()
                    .badgeHolderName(fullName)
                    .nameAtBirth(birthName)
                    .nino("NS123456A")
                    .dob(LocalDate.now())
                    .genderCode(GenderCodeField.FEMALE));

    Eligibility eligibility =
        new Eligibility()
            .typeCode(EligibilityCodeField.WALKD)
            .descriptionOfConditions(condDesc)
            .walkingDifficulty(
                new WalkingDifficulty()
                    .walkingLengthOfTimeCode(WalkingLengthOfTimeCodeField.LESSMIN)
                    .walkingSpeedCode(WalkingSpeedCodeField.SLOW)
                    .typeCodes(
                        Lists.newArrayList(
                            WalkingDifficultyTypeCodeField.PAIN,
                            WalkingDifficultyTypeCodeField.BALANCE))
                    .walkingAids(
                        Lists.newArrayList(
                            new WalkingAid()
                                .description("walk aid description")
                                .usage("walk aid usage")
                                .howProvidedCode(HowProvidedCodeField.PRESCRIBE))));

    return Application.builder()
        .applicationTypeCode(ApplicationTypeCodeField.NEW)
        .localAuthorityCode(la)
        .paymentTaken(false)
        .party(party)
        .eligibility(eligibility)
        .build();
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.DECLARATIONS;
  }
}
