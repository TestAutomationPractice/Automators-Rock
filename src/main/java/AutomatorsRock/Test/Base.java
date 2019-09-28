package AutomatorsRock.Test;
import java.lang.reflect.Method;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.ResultStatus;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;

public class Base {

	protected TestRunType testRunType;
	protected String userName;
	protected String password;
	protected UserRole userRole;
	
	protected ThreadLocal<CommonTestDataDto> commonTestData = new ThreadLocal<CommonTestDataDto>();

	@Parameters({ "platform", "browser", "version", "screenResolution" })
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(String platform, String browser, String version, String screenResolution, Method method)
			throws Exception {
		long threadId = Thread.currentThread().getId();
		TestDataHelper testDataHelper = new TestDataHelper();
		String testCaseNumber = getTestCaseNumber(method);
		Log4JLogger logger = new Log4JLogger();
		WebDriverHelper webDriverHelper = new WebDriverHelper();
		RemoteWebDriver driver = (RemoteWebDriver) webDriverHelper.getWebDriver(platform, browser, version,
				screenResolution, testCaseNumber, logger);

		CommonTestDataDto testDataDto = new CommonTestDataDto(null, testDataHelper, logger, driver, threadId);
		commonTestData.set(testDataDto);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result, Method method) throws Throwable {
		CommonTestDataDto testDataDto = commonTestData.get();
		WebDriverHelper webDriverHelper = new WebDriverHelper();
		CommonFunctions commonFunction = new CommonFunctions();
		String testCaseNumbers = getTestCaseNumber(method);
		String customDescription = getCustomDescriptionForQtest(result, method);
		Configuration configuration = Configuration.getInstance();
		QtestConfiguration qTestConfiguration = QtestConfiguration.getInstance();
		String[] testCase = testCaseNumbers.split(",");
		try{
			for(int i=0; i<testCase.length; i++){
				System.out.println("###############QTEST updated : "+ i + "NO: "+testCase[i].trim());
				try {
					if (!result.isSuccess()) {
						commonFunction.captureScreenshot(testDataDto.getDriver(), method.getName());
						testDataDto.getLogger().log(result.getThrowable().toString());
						if (configuration.getIsToEnableQtestIntegration()) {
							qTestConfiguration.updateStatus(testRunType, testCase[i].trim(), customDescription, ResultStatus.Fail);
						}
					} else {
						if (configuration.getIsToEnableQtestIntegration()) {
							qTestConfiguration.updateStatus(testRunType, testCase[i].trim(), customDescription, ResultStatus.Pass);
						}
					}
		
				} catch (Exception e) {
					if (configuration.getIsToEnableQtestIntegration()) {
						qTestConfiguration.updateStatus(testRunType, testCase[i].trim(), customDescription, ResultStatus.Fail);
					}
					throw e;
				} 
			}
		}catch(Exception e){
			throw e;
		}finally {
			webDriverHelper.closeWebDriver(testDataDto.getDriver());
		}
	}

	protected void navigateToURL(CommonTestDataDto testDataDto, String url)
			throws Exception {
		  WebDriver driver = testDataDto.getDriver();
		  Log4JLogger logger = testDataDto.getLogger();
		  driver.get(url);
		  logger.log("Method Called: Open URL");
	}
	
	public String generateNumInStringFormat(int length) {
		// *** Function to create random string
		String allowedChars = "0123456789";
		String text = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		text = temp.substring(0, temp.length());
		return text;
	}
	
	protected String getTestCaseNumber(Method method) {
		String testCaseNumber = null;
		if (method.getAnnotation(CustomTestAnnotations.class) != null) {
			testCaseNumber = method.getAnnotation(CustomTestAnnotations.class).testCaseNumber();
		} else if (method.getAnnotation(RoleBasedTestCaseMappings.class) != null) {
			RoleBasedTestCaseMapping[] mappings = method.getAnnotation(RoleBasedTestCaseMappings.class).value();
			RoleBasedTestCaseMapping mapping = CommonFunctions.findRoleBasedTestCaseMappings(mappings, userRole);
			testCaseNumber = mapping.testCaseNumber();
		}
		return testCaseNumber;
	}

	private String getCustomDescriptionForQtest(ITestResult result, Method method) {
		String customDescription = null;
		IRetryAnalyzer retryAnalyser = result.getMethod().getRetryAnalyzer();
		Boolean isRetried = false;
		Integer retryCount = 0;
		if (retryAnalyser instanceof TestCaseRetry) {
			// Check if the retry analyser's retry count was greater than zero.
			// If yes, then its a retried method
			retryCount = ((TestCaseRetry) retryAnalyser).getRetryCount();
			if (retryCount > 0) {
				isRetried = true;
			}
		}
		if (method.getAnnotation(CustomTestAnnotations.class) != null) {
			customDescription = null;
		} else if (method.getAnnotation(RoleBasedTestCaseMappings.class) != null) {
			customDescription = "User Role: " + userRole.name();
		}

		if (isRetried) {
			if (customDescription != null) {
				customDescription += "Retry-" + retryCount;
			} else {
				customDescription = "Retry-" + retryCount;
			}
		}
		return customDescription;
	}

	protected void navigateBackward(WebDriver driver) {
		driver.navigate().back();
	}
}
