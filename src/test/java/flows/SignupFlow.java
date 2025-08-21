package flows;

import dto.SignupEntity;
import io.qameta.allure.Allure;
import pages.SignupPage;

import static utils.Helper.stamp;

public class SignupFlow {
    private final SignupPage sp;

    public SignupFlow(SignupPage sp) { this.sp = sp; }

    public SignupPage happyPath(SignupEntity d) {
        sp.type(sp.getOrgInput(),d.organization(),false);
        sp.type(sp.getContactNameInput(),d.contactName(),false);
        sp.type(sp.getEmailInput(),d.email(),false);

        // use BasePage generic selects via exposed locators
        String chosenCountry = sp.selectRandomOption(sp.getCountrySelect(), true);
        String chosenType    = sp.selectRandomOption(sp.getAccountTypeSelect(), true);

        //Attach screenshots of the selected values
        Allure.addAttachment("Chosen Country", "text/plain", chosenCountry);
        Allure.addAttachment("Chosen Account Type", "text/plain", chosenType);

        sp.type(sp.getAccountNameInput(),d.accountName(),false);

        sp.click(sp.getSignUpBtn());
        return sp;
    }

}
