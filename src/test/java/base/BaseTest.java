package base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import config.ConfigLoader;
import org.testng.ITestResult;
import org.testng.annotations.*;
import core.PlaywrightFactory;

import java.lang.reflect.Method;

public abstract class BaseTest {
    protected Page page;

    private final int navRetries  = Integer.getInteger("nav.retries", 3);
    private final int navTimeout  = Integer.getInteger("nav.timeout", 40000);
    private final int defaultToMs = Integer.getInteger("pw.defaultTimeout", 15000);

    @BeforeSuite(alwaysRun = true)
    public void initiateBrowser() {
        PlaywrightFactory.init(); // browser only
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        page = PlaywrightFactory.newTestPage(method.getName());  // ← returns the Page
        page.setDefaultTimeout(defaultToMs);
        safeNavigate(ConfigLoader.get().baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        PlaywrightFactory.cleanup(!result.isSuccess(), result.getMethod().getMethodName());
    }

    @AfterSuite(alwaysRun = true)
    public void stopBrowser() {
        PlaywrightFactory.shutdown();
    }

    private void safeNavigate(String url) {
        RuntimeException lastErr = null;
        for (int i = 1; i <= navRetries; i++) {
            try {
                // navigate and wait until DOM content is loaded
                page.navigate(url, new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(navTimeout));

                // wait until the document is fully loaded
                try {
                    page.waitForFunction("() => document.readyState === 'complete'",
                            new Page.WaitForFunctionOptions().setTimeout(5000));
                } catch (Exception ignored) {}

                // short network idle settle
                try {
                    page.waitForLoadState(LoadState.NETWORKIDLE,
                            new Page.WaitForLoadStateOptions().setTimeout(1500));
                } catch (Exception ignored) {}

                return; // success
            } catch (PlaywrightException e) {
                lastErr = e;
                page.waitForTimeout(700);
            }
        }
        throw (lastErr != null ? lastErr : new RuntimeException("Navigation failed"));
    }

}
