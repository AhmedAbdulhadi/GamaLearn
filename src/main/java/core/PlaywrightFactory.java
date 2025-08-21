package core;

import com.microsoft.playwright.*;
import config.ConfigLoader;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlaywrightFactory {
    private static Playwright playwright;
    public static Browser browser;

    // test-scoped
    public static BrowserContext context;
    public static Page page;

    public static Page currentPage() { return page; }
    public static BrowserContext currentContext() { return context; }

    // artifacts
    private static final Path ARTIFACTS = Paths.get("artifacts");
    private static final Path SHOTS     = ARTIFACTS.resolve("screens");

    // current run modes (no getters from Playwright, so store them here)
    private static String traceMode   = "on";
    private static String shotMode    = "only-on-failure";

    public static void init() {
        if (playwright != null) return;

        try {
            Files.createDirectories(SHOTS);
        } catch (Exception ignored) {}

        var cfg = ConfigLoader.get();
        String browserName = System.getProperty("browser",  cfg.browser());
        boolean headless   = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(cfg.headless())));

        playwright = Playwright.create();
        BrowserType.LaunchOptions launch = new BrowserType.LaunchOptions().setHeadless(headless);
        browser = switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(launch);
            case "webkit"  -> playwright.webkit().launch(launch);
            default        -> playwright.chromium().launch(launch);
        };
    }

    /** Create a fresh context+page per test. */
    public static Page newTestPage(String testName) {
        var cfg = ConfigLoader.get();
        String device = System.getProperty("device", cfg.device());
        traceMode     = System.getProperty("trace", cfg.trace());              // on | off
        shotMode      = System.getProperty("screenshot", cfg.screenshot());    // on | off | only-on-failure

        // close any previous test context just in case
        try { if (context != null) context.close(); } catch (Exception ignored) {}

        Browser.NewContextOptions opts = buildOptions(device);
        context = browser.newContext(opts);

        if ("on".equalsIgnoreCase(traceMode)) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true).setSnapshots(true).setSources(true));
        }

        page = context.newPage();
        return page;
    }

    private static Browser.NewContextOptions buildOptions(String device) {
        Browser.NewContextOptions o = new Browser.NewContextOptions();
        if (device == null || device.isBlank()) return o; // desktop
        return switch (device) {
            case "iPhone 13" -> o.setViewportSize(390, 844).setDeviceScaleFactor(3)
                    .setIsMobile(true).setHasTouch(true)
                    .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1");
            case "Pixel 5" -> o.setViewportSize(393, 851).setDeviceScaleFactor(2.75)
                    .setIsMobile(true).setHasTouch(true)
                    .setUserAgent("Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
            default -> throw new IllegalArgumentException("Unknown device: " + device);
        };
    }

    /** Stop trace, take screenshot (respecting mode), close context. */
    public static void cleanup(boolean failed, String testName) {
        try {
            if (context != null && "on".equalsIgnoreCase(traceMode)) {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(ARTIFACTS.resolve("trace-" + testName + (failed ? "-failed" : "") + ".zip")));
            }
        } catch (PlaywrightException ignored) {}

        try {
            if (page != null) {
                boolean take = "on".equalsIgnoreCase(shotMode) ||
                        ("only-on-failure".equalsIgnoreCase(shotMode) && failed);
                if (take) {
                    String stamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
                    page.screenshot(new Page.ScreenshotOptions()
                            .setFullPage(true)
                            .setPath(SHOTS.resolve(testName + "-" + stamp + (failed ? "-failed" : "") + ".png")));
                }
            }
        } catch (Exception ignored) {}

        try { if (context != null) context.close(); } catch (Exception ignored) {}

        context = null; page = null;
    }

    /** Close browser & Playwright once per suite. */
    public static void shutdown() {
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
        browser = null; playwright = null;
    }
}
