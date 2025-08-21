package dto;

public record SignupEntity(
        String organization,
        String contactName,
        String email,
        String countryLabel,
        String accountTypeLabel,
        String accountName,   // must be 5 chars: "qa" + 3 digits (qa###)
        String captchaValue   // dummy if captcha visible
) {}
