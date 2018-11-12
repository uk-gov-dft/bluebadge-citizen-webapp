package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AlreadyHaveBlueBadgePage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.BenifitsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class BenefitsSteps extends AbstractSpringSteps {

    private CommonSteps commonSteps;
    private ChooseCouncilSteps chooseCouncilSteps;
    private CommonPage commonPage;
    private ApplicationFixture applicationFixture;

    @Autowired
    public BenefitsSteps(CommonPage commonPage, CommonSteps commonSteps) {
        this.commonPage = commonPage;
        this.commonSteps = commonSteps;
    }

    @And("^I validate \"(you|they)\" benefit page for \"(PIP|DLA|AFRFCS|WPMS|NONE)\"")
    public void iValidateBenefitPageFor(String applicant, String option)  {
        verifyPageContent(applicant);
        commonSteps.iVerifyValidationMessage(BenifitsPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

        if ("PIP".equals(option)) {
            commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST);
        } else {
            commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST + "." + option);
        }
        commonSteps.iClickOnContinueButton();
    }

    public void verifyPageContent(String applicant) {
        if ("you".equals(applicant.toLowerCase())) {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(BenifitsPage.PAGE_TITLE_YOURSELF);
            commonSteps.iShouldSeeTheHeading(BenifitsPage.PAGE_TITLE_YOURSELF);
        } else {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
                    BenifitsPage.PAGE_TITLE_SOMEONE_ELSE);
            commonSteps.iShouldSeeTheHeading(BenifitsPage.PAGE_TITLE_SOMEONE_ELSE);
        }
    }



}
