package uk.gov.service.bluebadge.test.acceptance.steps.site;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.EnterCardDetailsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class EnterCardDetailsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public EnterCardDetailsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("I complete enter cards details page and press continue")
  public void iCompleteBadgePaymentPageWithPayOnlineNow() {
    commonPage
        .findPageElementById(EnterCardDetailsPage.CARD_NUMBER_ID)
        .sendKeys("4444333322221111");
    commonPage.findPageElementById(EnterCardDetailsPage.EXPIRY_MONTH_ID).sendKeys("01");
    commonPage.findPageElementById(EnterCardDetailsPage.EXPIRY_YEAR_ID).sendKeys("22");
    commonPage.findPageElementById(EnterCardDetailsPage.NAME_ON_CARD_ID).sendKeys("Full name");
    commonPage.findPageElementById(EnterCardDetailsPage.CARD_SECURITY_CODE_ID).sendKeys("111");
    commonPage
        .findPageElementById(EnterCardDetailsPage.COUNTRY_OR_TERRITORY_ID)
        .sendKeys("United Kingdom");
    commonPage
        .findPageElementById(EnterCardDetailsPage.BUILDING_NUMBER_OR_NAME_AND_STREET_ID)
        .sendKeys("address line 1");
    commonPage.findPageElementById(EnterCardDetailsPage.LINE2_ID).sendKeys("address line 2");
    commonPage.findPageElementById(EnterCardDetailsPage.TOWN_OR_CITY_ID).sendKeys("Town");
    commonPage.findPageElementById(EnterCardDetailsPage.POSTCODE_ID).sendKeys("M41FS");
    commonPage
        .findPageElementById(EnterCardDetailsPage.EMAIL_ID)
        .sendKeys("testpayments1@mailinator.com");

    commonPage.findPageElementById(EnterCardDetailsPage.CONTINUE_BUTTON_ID).click();
  }

  @And("I don't complete enter cards details page and press cancel payment")
  public void iCompleteBadgePaymentPageWithSubmitApplicationAndPayLater() {
    commonPage.findPageElementById(EnterCardDetailsPage.CANCEL_PAYMENT_LINK_ID).click();
  }

  @And("I should see payment description \"([^\"]+)\"")
  public void iShouldSeePaymentDescription(String paymentDescription) {
    assertThat(
        commonPage.findPageElementById(EnterCardDetailsPage.PAYMENT_DESCRIPTION_ID).getText(),
        is(paymentDescription));
  }

  @And("I should see badge cost \"([^\"]+)\"")
  public void iShouldSeeBadgeCost(String badgeCost) {
    assertThat(
        commonPage.findPageElementById(EnterCardDetailsPage.AMOUNT_ID).getText(), is(badgeCost));
  }

  @When("^I confirm payment$")
  public void iConfirmPayment() {
    commonPage.findPageElementById(EnterCardDetailsPage.CONTINUE_BUTTON_ID).click();
  }
}
