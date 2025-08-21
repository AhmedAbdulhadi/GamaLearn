package utils;

import dto.SignupEntity;
import pages.SignupPage;
public class SignupHelper {
    public static SignupEntity validRandom(SignupPage sp) {
        String uniq = Helper.timestamp();
        String country = sp.selectRandomOption(sp.getCountrySelect(), true);
        String accountType = sp.selectRandomOption(sp.getAccountTypeSelect(), true);

        return new SignupEntity(
                "QA Org " + uniq,
                "Tester " + uniq.substring(Math.max(0, uniq.length() - 6)),
                "signup+" + uniq + "@example.test",
                country,
                accountType,
                "qa" + Helper.digits(3),
                "ABCDE"
        );
    }

}
