package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.LocalAuthorityPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class LocalAuthoritySteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;

  @Autowired
  public LocalAuthoritySteps(CommonSteps commonSteps) {
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate local authority page for \"(yourself|someone else)\" in \"(england|wales|scotland)\"")
  public void iValidateLocalAuthorityPage(String applicant, String country) {
    verifyPageContent(chooseCouncil(country), applicant);
    commonSteps.iClickOnContinueButton();
  }

  private String chooseCouncil(String country) {
    String fullCouncil = "Worcestershire county council";
    if ("scotland".equalsIgnoreCase(country)) {
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      fullCouncil = "Isle of Anglesey county council";
    }
    return fullCouncil;
  }

  public void verifyPageContent(String la, String applicant) {
    commonSteps.iShouldSeeTheCorrectURL(LocalAuthorityPage.PAGE_URL);
    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(LocalAuthorityPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(LocalAuthorityPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTextOnPage(la);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          LocalAuthorityPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(LocalAuthorityPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTextOnPage(la);
    }
  }
}
