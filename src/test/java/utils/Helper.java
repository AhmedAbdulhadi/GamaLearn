package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public final class Helper {
    private Helper() {}
    private static final Random R = new Random();

    public static String stamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String slug(String seed) {
        String base = (seed == null ? "qa" : seed.toLowerCase(Locale.ROOT))
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return base + "-" + stamp();
    }

    public static String email(String local) {
        return local.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", ".")
                + "+" + stamp() + "@example.test";
    }

    public static String shortUuid(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, Math.max(4, Math.min(12, len)));
    }

    public static String timestamp() {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
    }
    public static String digits(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(R.nextInt(10));
        return sb.toString();
    }
}
