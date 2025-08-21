package tests;

import base.BaseTest;
import dto.SignupEntity;
import flows.SignupFlow;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignupPage;
import core.RetryAnalyzer;
import utils.SignupHelper;

public class SignupTest extends BaseTest {


    /** Test Case 001 **/
    @Test(description = "Full scenario", retryAnalyzer = RetryAnalyzer.class)
    public void signup_happy_path() {
        SignupPage sp = new SignupPage(page);
        SignupEntity data = SignupHelper.validRandom(sp);
        new SignupFlow(sp).happyPath(data);

        sp.ready(sp.getSuccessPanel());
        Assert.assertTrue(sp.isVisible(sp.getSuccessIcon(),500));
        Assert.assertTrue(sp.isVisible(sp.getSuccessHeader(),500));
        Assert.assertTrue(sp.isVisible(sp.getSuccessDesc(),500));
    }

    /** Test Case 002 **/
    @Test(description = "Empty submit shows required field validations", retryAnalyzer = RetryAnalyzer.class)
    public void submitEmpty_ShowsRequiredValidations() {
        SignupPage sp = new SignupPage(page);
        sp.clear(sp.getAccountNameInput());
        sp.click(sp.getSignUpBtn());

        Assert.assertTrue(sp.isVisible(sp.getContactRequiredMsg(), 1000), "Contact name required message should be visible");
        Assert.assertTrue(sp.isVisible(sp.getEmailRequiredMsg(), 1000), "Contact name required message should be visible");
        Assert.assertTrue(sp.isVisible(sp.getSiteRequiredMsg(), 1000), "Contact name required message should be visible");

    }

    /** Test Case 003 **/
    @Test(description = "Invalid email format shows validation and message is correct", retryAnalyzer = RetryAnalyzer.class)
    public void invalidEmailMessage_ShowsValidation() {
        SignupPage sp = new SignupPage(page);

        sp.type(sp.getOrgInput(),"GamaLearn",true);
        sp.type(sp.getContactNameInput(), "Ahmed Wajieh",true);
        sp.type(sp.getEmailInput(),"invalid-email",true);

        sp.click(sp.getSignUpBtn());
        Assert.assertTrue(sp.isVisible(sp.getEmailInvalidMsg(),500), "Invalid emailsss message should be visible");
    }

    /** Test Case 004 **/
    @Test(description = "AccountName length validation (client-side)",  retryAnalyzer = RetryAnalyzer.class)
    public void shortAccountName_ShowsLengthValidation() {
        SignupPage sp = new SignupPage(page);

        sp.type(sp.getOrgInput(),"GamaLearn",true);
        sp.type(sp.getContactNameInput(), "Ahmed Wajieh",true);
        sp.type(sp.getEmailInput(),"invalid-email",true);
        sp.type(sp.getAccountNameInput(),"abc",true);

        sp.click(sp.getSignUpBtn());
        Assert.assertTrue(sp.isVisible(sp.getAccountNameLengthMsg(),500), "Account name length message should be visible");
    }

}
