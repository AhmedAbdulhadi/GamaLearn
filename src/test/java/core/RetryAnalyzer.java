package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int attempt = 0;
    private static final int MAX_RETRY = 0; //Add how many retries you want to apply after faiulres e.g. 1 will run again one more time.

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < MAX_RETRY) { attempt++; return true; }
        return false;
    }
}
