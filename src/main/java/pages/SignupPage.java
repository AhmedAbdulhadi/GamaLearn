package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public class SignupPage extends BasePage {

    private final Locator orgInput = page.locator("input[name='SignUp1$txtOrganization']");
    private final Locator contactNameInput = page.locator("input[name='SignUp1$txtName']");
    private final Locator emailInput = page.locator("input[name='SignUp1$txtEmail']");
    private final Locator countrySelect = page.locator("select[name='SignUp1$ddlCountry']");
    private final Locator accountNameInput = page.locator("input[name='SignUp1$txtAccountURL']");
    private final Locator accountTypeSelect = page.locator("select[name='SignUp1$ddlAccountType']");
    private final Locator captchaImage = page.locator("#SignUp1_signUpCaptcha_CaptchaImageUP");
    private final Locator captchaInput = page.locator("#SignUp1_signUpCaptcha_CaptchaTextBox");
    private final Locator signUpBtn = page.locator("input[type='submit'][name='SignUp1$btnSignUp'], #SignUp1_btnSignUp")
            .or(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign Up")));
    private final Locator successPanel = page.locator("div.formRow.animated.fadeInBig");
    private final Locator successIcon = page.locator("ul.middleNavR li i.icon-checkmark");
    private final Locator successHeader = page.locator("#SignUp1_litSuccess_HeadPanel");
    private final Locator successDesc = page.locator("#SignUp1_litSuccess_DescPanel");
    private final Locator successToast = page.getByText("Thanks for signing up", new Page.GetByTextOptions().setExact(false));
    private final Pattern successUrlPart = Pattern.compile("SignupSuccess|Onboarding|Welcome",
            Pattern.CASE_INSENSITIVE);
    private final Locator emailRequiredMsg     = page.getByText("Email is required", new Page.GetByTextOptions().setExact(false));
    private final Locator emailInvalidMsg      = page.getByText("Invalids emailsss", new Page.GetByTextOptions().setExact(false));
    private final Locator contactRequiredMsg   = page.getByText("Contact name is required", new Page.GetByTextOptions().setExact(false));
    private final Locator siteRequiredMsg      = page.getByText("Site Address is required", new Page.GetByTextOptions().setExact(false));
    private final Locator accountNameLengthMsg = page.getByText("at least 5 characters", new Page.GetByTextOptions().setExact(false));


    public Locator getCountrySelect() {
        return countrySelect;
    }

    public Locator getOrgInput() {
        return orgInput;
    }

    public Locator getContactNameInput() {
        return contactNameInput;
    }

    public Locator getEmailInput() {
        return emailInput;
    }

    public Locator getAccountNameInput() {
        return accountNameInput;
    }

    public Locator getAccountTypeSelect() {
        return accountTypeSelect;
    }

    public Locator getCaptchaImage() {
        return captchaImage;
    }

    public Locator getCaptchaInput() {
        return captchaInput;
    }

    public Locator getSignUpBtn() {
        return signUpBtn;
    }

    public Locator getSuccessPanel() {
        return successPanel;
    }

    public Locator getSuccessIcon() {
        return successIcon;
    }

    public Locator getSuccessHeader() {
        return successHeader;
    }

    public Locator getSuccessDesc() {
        return successDesc;
    }

    public Locator getSuccessToast() {
        return successToast;
    }

    public Pattern getSuccessUrlPart() {
        return successUrlPart;
    }

    public Locator getEmailRequiredMsg() {
        return emailRequiredMsg;
    }

    public Locator getEmailInvalidMsg() {
        return emailInvalidMsg;
    }

    public Locator getContactRequiredMsg() {
        return contactRequiredMsg;
    }

    public Locator getSiteRequiredMsg() {
        return siteRequiredMsg;
    }

    public Locator getAccountNameLengthMsg() {
        return accountNameLengthMsg;
    }

    public SignupPage(Page page) {
        super(page);

    }

}
