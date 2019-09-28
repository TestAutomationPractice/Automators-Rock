/**
 * 
 */
package Miscellaneous;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author devesh
 *
 */
public class TestCaseRetry implements IRetryAnalyzer {
	private AtomicInteger counter = new AtomicInteger(0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 */
	@Override
	public boolean retry(ITestResult result) {
		Boolean isToRetry = false;
		if (!result.isSuccess()) {
			if ((counter.get() < Configuration.getInstance().getmaxRetryCount()) && result.getThrowable().getClass()
					.equals(org.openqa.selenium.remote.UnreachableBrowserException.class)) {
				isToRetry = true;
			}
			counter.incrementAndGet();
		}
		return isToRetry;
	}

	public synchronized int getRetryCount() {
		return counter.get() - 1;
	}

}
