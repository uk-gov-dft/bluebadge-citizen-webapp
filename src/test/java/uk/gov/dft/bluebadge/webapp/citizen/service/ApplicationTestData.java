package uk.gov.dft.bluebadge.webapp.citizen.service;

import com.google.common.collect.Lists;
import java.time.LocalDate;
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

public class ApplicationTestData {

  public static Contact APPLICATION_CONTACT =
      new Contact()
          .buildingStreet("65 Basil Chambers")
          .line2("Northern Quarter")
          .townCity("Manchester")
          .postCode("SK6 8GH")
          .primaryPhoneNumber("016111234567")
          .secondaryPhoneNumber("079707777111")
          .emailAddress("nobody@thisisatestabc.com");

  public static Person APPLICATION_PERSON =
      new Person()
          .badgeHolderName("John Smith")
          .nino("NS123456A")
          .dob(LocalDate.now())
          .genderCode(GenderCodeField.FEMALE);

  public static Party APPLICATION_PARTY =
      new Party()
          .typeCode(PartyTypeCodeField.PERSON)
          .contact(APPLICATION_CONTACT)
          .person(APPLICATION_PERSON);

  public static Eligibility APPLICATION_ELIGIBILITY =
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

  public static Application APPLICATION =
      Application.builder()
          .applicationTypeCode(ApplicationTypeCodeField.NEW)
          .localAuthorityCode("ABERD")
          .paymentTaken(false)
          .party(APPLICATION_PARTY)
          .eligibility(APPLICATION_ELIGIBILITY)
          .build();

  public static Application BAD_APPLICATION =
      Application.builder()
          .applicationTypeCode(ApplicationTypeCodeField.NEW)
          .localAuthorityCode("ABERD")
          .paymentTaken(false)
          .build();
}
