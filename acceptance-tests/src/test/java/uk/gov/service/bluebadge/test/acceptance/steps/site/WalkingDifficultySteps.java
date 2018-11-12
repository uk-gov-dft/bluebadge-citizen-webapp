package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WalkingDifficultyPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class WalkingDifficultySteps extends AbstractSpringSteps {

    private CommonSteps commonSteps;
    private CommonPage commonPage;


    @Autowired
    public WalkingDifficultySteps(CommonPage commonPage, CommonSteps commonSteps) {
        this.commonPage = commonPage;
        this.commonSteps = commonSteps;
    }

    @And("^I validate \"(you|them)\" walking difficulty page for \"(HELP|NONE)\"")
    public void iValidateWalkingDifficultyPageFor(String applicant, String option) {
        verifyPageContent(applicant);
        commonSteps.iVerifyValidationMessage(WalkingDifficultyPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

        if ("help".equalsIgnoreCase(option)) {
            commonPage.findPageElementById(WalkingDifficultyPage.WALKING_DIFFICULTIES_LIST ).click();
        } else {
            commonPage.findPageElementById(WalkingDifficultyPage.WALKING_DIFFICULTIES_LIST + "." + option).click();
        }

        commonSteps.iClickOnContinueButton();
    }

    public void verifyPageContent(String applicant) {

        commonSteps.iShouldSeeTheCorrectURL(WalkingDifficultyPage.PAGE_URL);

        if ("you".equals(applicant.toLowerCase())) {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
            commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
        } else {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
                    WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
            commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
        }
    }


}
