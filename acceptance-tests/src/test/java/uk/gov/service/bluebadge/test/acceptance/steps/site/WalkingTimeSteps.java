package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WalkingTimePage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class WalkingTimeSteps extends AbstractSpringSteps {

    private CommonSteps commonSteps;
    private CommonPage commonPage;

    @Autowired
    public WalkingTimeSteps(CommonPage commonPage, CommonSteps commonSteps) {
        this.commonPage = commonPage;
        this.commonSteps = commonSteps;
    }


    @And("^I validate walking time page for a \"([^\"]*)\" application for \"(CANTWALK|LESSMIN|FEWMIN|MORETEN)\"")
    public void iValidateWalkingTimePage(String applicant, String option) {
        verifyPageContent(applicant);
        commonSteps.iVerifyValidationMessage(WalkingTimePage.VALIDATION_MESSAGE_FOR_NO_OPTION);

        enterValidValuesAndContinue(option);
    }

    public void verifyPageContent(String applicant) {

        commonSteps.iShouldSeeTheCorrectURL(WalkingTimePage.PAGE_URL);

        if ("yourself".equals(applicant.toLowerCase())) {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
                    WalkingTimePage.PAGE_TITLE_YOURSELF);
            commonSteps.iShouldSeeTheHeading(WalkingTimePage.PAGE_TITLE_YOURSELF);
        } else {
            commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
                    WalkingTimePage.PAGE_TITLE_SOMEONE_ELSE);
            commonSteps.iShouldSeeTheHeading(WalkingTimePage.PAGE_TITLE_SOMEONE_ELSE);
        }
    }

    public void enterValidValuesAndContinue(String difficulty) {

        if ("CANTWALK".equals(difficulty)) {
            commonPage.selectRadioButton(WalkingTimePage.WALKING_TIME_LIST);
        } else {
            commonPage.selectRadioButton(
                    WalkingTimePage.WALKING_TIME_LIST + "." + difficulty);
        }

        commonSteps.iClickOnContinueButton();
    }


}
