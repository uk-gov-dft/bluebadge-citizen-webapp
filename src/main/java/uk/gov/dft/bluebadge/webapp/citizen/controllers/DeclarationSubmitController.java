package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.view.ErrorViewModel;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

@Controller
public class DeclarationSubmitController extends BaseController {

  public static final String URL_DECLARATION = "/apply-for-a-blue-badge/declaration";
  public static final String TEMPLATE_DECLARATION = "application-end/declaration";

  public static final String URL_APPLICATION_SUBMITTED = "/application-submitted";
  public static final String TEMPLATE_APPLICATION_SUBMITTED = "application-end/submitted";

  private ApplicationManagementService appService;

  public DeclarationSubmitController(ApplicationManagementService appService) {
    this.appService = appService;
  }

  @GetMapping(URL_DECLARATION)
  public String showDeclaration(
      @Valid @ModelAttribute("formRequest") DeclarationForm formRequest, Model model) {

    return TEMPLATE_DECLARATION;
  }

  @PostMapping(URL_DECLARATION)
  public String submitDeclaration(
      @Valid @ModelAttribute("formRequest") DeclarationForm formRequest,
      BindingResult bindingResult,
      Model model) {

    model.addAttribute("errorSummary", new ErrorViewModel());

    if (bindingResult.hasErrors()) {
      return TEMPLATE_DECLARATION;
    }

    Application app = getDummyApplication();
    appService.create(app);

    return "redirect:" + URL_APPLICATION_SUBMITTED;
  }

  private Application getDummyApplication() {
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
                    .badgeHolderName("John Smith")
                    .nino("NS123456A")
                    .dob(LocalDate.now())
                    .genderCode(GenderCodeField.FEMALE));

    Eligibility eligibility =
        new Eligibility()
            .typeCode(EligibilityCodeField.WALKD)
            .descriptionOfConditions("This is a description")
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
        .localAuthorityCode("ABERD")
        .paymentTaken(false)
        .party(party)
        .eligibility(eligibility)
        .build();
  }

  @GetMapping(URL_APPLICATION_SUBMITTED)
  public String showSubmitted() {
    return TEMPLATE_APPLICATION_SUBMITTED;
  }
}
