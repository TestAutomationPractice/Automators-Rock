package Miscellaneous;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.qas.qtest.api.services.design.model.TestCase;
import org.qas.qtest.api.services.execution.model.TestCycle;
import org.qas.qtest.api.services.execution.model.TestSuite;
import org.qas.qtest.api.services.project.model.Project;
import Miscellaneous.Enums.UserRole;

/**
 * Provides common functions used across Test cases.
 *
 */
public final class CommonFunctions {

		public static Project findProjectInQtest(List<Project> projects, String projectName) {
		for (Project project : projects) {
			if (project.getName().equals(projectName)) {
				return project;
			}
		}
		return null;
	}

	public static TestCase findTestCaseInQtest(List<TestCase> testCases, String testCaseName) {
		for (TestCase testCase : testCases) {
			if (testCase.getPid().equals(testCaseName)) {
				return testCase;
			}
		}
		return null;
	}

	public static TestCycle findTestCyclesInQtest(List<TestCycle> testCycles, String testCycleName) {
		for (TestCycle testCycle : testCycles) {
			if (testCycle.getName().equals(testCycleName)) {
				return testCycle;
			}
		}
		return null;
	}

	public static TestSuite findTestSuitesInQtest(List<TestSuite> testSuites, String testSuiteName) {
		for (TestSuite testSuite : testSuites) {
			System.out.println(testSuite.getName());
			if (testSuite.getName().equals(testSuiteName)) {
				return testSuite;
			}
		}
		return null;
	}

	public static RoleBasedTestCaseMapping findRoleBasedTestCaseMappings(RoleBasedTestCaseMapping[] mappings,
			UserRole userRole) {
		for (RoleBasedTestCaseMapping mapping : mappings) {

			if (mapping.userRole().equals(userRole)) {
				return mapping;
			}
		}
		return null;
	}

	public String captureScreenshot(WebDriver driver, String screenshotName) throws IOException {	
			WebDriver augment = new Augmenter().augment(driver);
			TakesScreenshot ts = (TakesScreenshot) augment;
			File source = ts.getScreenshotAs(OutputType.FILE);
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String formattedDate = dateFormat.format(new Date());
			new File(System.getProperty("user.dir") + File.separator + "screenShots" + File.separator + formattedDate);
			String dest = System.getProperty("user.dir") + File.separator + "screenShots" + File.separator + formattedDate + File.separator + screenshotName + ".png";
			File destination = new File(dest);
			FileUtils.copyFile(source, destination);
			System.out.println("Screenshot taken for failed test case.");
			return dest;
	}

}
