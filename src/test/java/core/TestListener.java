package core;

import com.microsoft.playwright.Page;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Page page = PlaywrightFactory.currentPage();
            if (page == null || page.isClosed()) return;

            // still save a file if you want a local copy
            Path shotsDir = Paths.get("artifacts", "screens");
            Files.createDirectories(shotsDir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = result.getMethod().getMethodName() + "_" + stamp + ".png";
            Path path = shotsDir.resolve(filename);

            byte[] png = page.screenshot(new Page.ScreenshotOptions()
                    .setPath(path)
                    .setFullPage(true));

            // attach to Allure
            io.qameta.allure.Allure.addAttachment(
                    "Failure screenshot",
                    "image/png",
                    new java.io.ByteArrayInputStream(png),
                    "png"
            );
        } catch (Exception ignored) { }
    }

}
