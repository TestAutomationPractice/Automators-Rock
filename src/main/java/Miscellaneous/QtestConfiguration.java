package Miscellaneous;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import org.qas.qtest.api.auth.PropertiesQTestCredentials;
import org.qas.qtest.api.auth.QTestCredentials;
import org.qas.qtest.api.internal.model.ArtifactLevel;
import org.qas.qtest.api.services.design.TestDesignServiceClient;
import org.qas.qtest.api.services.design.model.ListTestCaseRequest;
import org.qas.qtest.api.services.design.model.TestCase;
import org.qas.qtest.api.services.execution.TestExecutionServiceClient;
import org.qas.qtest.api.services.execution.model.AutomationTestLog;
import org.qas.qtest.api.services.execution.model.AutomationTestLogRequest;
import org.qas.qtest.api.services.execution.model.CreateTestCycleRequest;
import org.qas.qtest.api.services.execution.model.CreateTestRunRequest;
import org.qas.qtest.api.services.execution.model.CreateTestSuiteRequest;
import org.qas.qtest.api.services.execution.model.ListTestCycleRequest;
import org.qas.qtest.api.services.execution.model.TestCycle;
import org.qas.qtest.api.services.execution.model.TestRun;
import org.qas.qtest.api.services.execution.model.TestSuite;
import org.qas.qtest.api.services.project.ProjectServiceClient;
import org.qas.qtest.api.services.project.model.ListProjectRequest;
import org.qas.qtest.api.services.project.model.Project;
import Miscellaneous.Enums.ResultStatus;
import Miscellaneous.Enums.TestRunType;

/**
 * Provides Functionality to update status in Qtest.
 *
 */
public class QtestConfiguration {

	private static QtestConfiguration _instance;
	private final QTestCredentials credentials;
	private Project _project;
	private ProjectServiceClient _projectService;
	private TestDesignServiceClient _testDesignService;
	private TestExecutionServiceClient _testExecutionService;
	private List<TestCase> _allTestCases;
	private TestCycle _buildSpecificTestCycle;
	private final String AUTOMATION_ROOT_FOLDER = "Automation";
	private TestSuite _regressionTestCycle;
	private TestSuite _smokeTestCycle;
	private static Object _locker = new Object();

	private QtestConfiguration() throws IOException {
		try {
			InputStream input = new FileInputStream("qTestCredentials.properties");
			credentials = new PropertiesQTestCredentials(input);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		loadProjectSummary();
		loadTestCases(_project.getId());
		loadEnvironmentFolder(_project.getId());
	}

	public static QtestConfiguration getInstance() throws IOException {
		if (Configuration.getInstance().getIsToEnableQtestIntegration() && _instance == null) {
			synchronized (_locker) {
				if (_instance == null) {
					_instance = new QtestConfiguration();
				}
			}
		}
		return _instance;
	}

	private void loadProjectSummary() {
		_projectService = new ProjectServiceClient(credentials);
		ListProjectRequest listProjectRequest = new ListProjectRequest();

		List<Project> projects = _projectService.listProject(listProjectRequest);

		_project = CommonFunctions.findProjectInQtest(projects,
				Configuration.getInstance().getQtestProjectName());
	}

	private void loadTestCases(long projectId) {
		_testDesignService = new TestDesignServiceClient(credentials);

		ListTestCaseRequest listTestCaseRequest = new ListTestCaseRequest().withProjectId(projectId).withPageSize(1000L)
				.withPage(1L);
		_allTestCases = _testDesignService.listTestCase(listTestCaseRequest);

	}

	private void loadEnvironmentFolder(long projectId) {

		_testExecutionService = new TestExecutionServiceClient(credentials);
		ListTestCycleRequest testCycleRequest = new ListTestCycleRequest().withProjectId(projectId);

		List<TestCycle> testCycles = _testExecutionService.listTestCycle(testCycleRequest);
		TestCycle testCycle = CommonFunctions.findTestCyclesInQtest(testCycles, AUTOMATION_ROOT_FOLDER);

		ListTestCycleRequest testCycleEnvironmentRequest = new ListTestCycleRequest().withProjectId(projectId)
				.withArtifactLevel(ArtifactLevel.TEST_CYCLE).withArtifactId(testCycle.getId());
		List<TestCycle> testCyclesEnvironmentSpecific = _testExecutionService
				.listTestCycle(testCycleEnvironmentRequest);
		TestCycle testCycleEnvironment = CommonFunctions.findTestCyclesInQtest(testCyclesEnvironmentSpecific,
				Configuration.getInstance().getEnvironmentName());
		System.out.println("Environment Name: " + Configuration.getInstance().getEnvironmentName());
		if (testCycleEnvironment != null) {
			System.out.println("Environment specific test cycle present");
		} else {
			System.out.println("Environment specific test cycle not present");
		}

		// get environment specific
		_buildSpecificTestCycle = createTestCycle(_project.getId(), testCycleEnvironment);
	}

	private TestCycle createTestCycle(long projectId, TestCycle parentTestCycle) {
		TestCycle testCycle = new TestCycle();
		testCycle.setName(Configuration.getInstance().getBuildIdentifier());
		CreateTestCycleRequest createtestCycleRequest = new CreateTestCycleRequest().withProjectId(projectId)
				.withArtifactId(parentTestCycle.getId()).withArtifactLevel(ArtifactLevel.TEST_CYCLE)
				.withTestCycle(testCycle);
		return _testExecutionService.createTestCycle(createtestCycleRequest);

	}

	private TestSuite createTestSuite(long projectId, TestCycle parentTestCycle, TestRunType testRunType) {
		TestSuite testSuite = new TestSuite();
		testSuite.setName(testRunType.name());
		CreateTestSuiteRequest testSuiteRequest = new CreateTestSuiteRequest().withProjectId(projectId)
				.withArtifactId(parentTestCycle.getId()).withArtifactLevel(ArtifactLevel.TEST_CYCLE)
				.withTestSuite(testSuite);

		return _testExecutionService.createTestSuite(testSuiteRequest);
	}

	private TestSuite initialiseTestSuite(TestRunType testRunType) {
		switch (testRunType) {
		case Regression:
			if (_regressionTestCycle == null) {
				synchronized (_locker) {
					if (_regressionTestCycle == null) {
						_regressionTestCycle = createTestSuite(_project.getId(), _buildSpecificTestCycle, testRunType);
					}
				}
			}
			return _regressionTestCycle;

		case Smoke:
			if (_smokeTestCycle == null) {
				synchronized (_locker) {
					if (_smokeTestCycle == null) {
						_smokeTestCycle = createTestSuite(_project.getId(), _buildSpecificTestCycle, testRunType);
					}
				}
			}
			return _smokeTestCycle;
		default:
			throw new InvalidParameterException("invalid testRunType: " + testRunType.name());
		}
	}

	/**
	 * Updates Test Case Status in Qtest.
	 * 
	 * @param testRunType
	 *            provides Test Run Type.
	 * @param testCaseId
	 *            provides Test Case Id that was executed as part of Automation.
	 * @param resultStatus
	 *            updates result of TestCase as passed or failed.
	 */
	public void updateStatus(TestRunType testRunType, String testCaseId, String description,
			ResultStatus resultStatus) {
		TestSuite testSuite = initialiseTestSuite(testRunType);

		TestCase testCase = CommonFunctions.findTestCaseInQtest(_allTestCases, testCaseId);
		TestRun testRun = new TestRun();
		if (description != null) {
			testRun.setName("[" + description + "]" + testCase.getName());
		} else {
			testRun.setName(testCase.getName());
		}

		testRun.setTestCase(testCase);
		CreateTestRunRequest testRunRequest = new CreateTestRunRequest().withProjectId(_project.getId())
				.withTestRun(testRun).withArtifactId(testSuite.getId()).withArtifactLevel(ArtifactLevel.TEST_SUITE);

		testRun = _testExecutionService.createTestRun(testRunRequest);

		AutomationTestLog automationTestLog = new AutomationTestLog().withExecutionStartDate(new Date())
				.withExecutionEndDate(new Date()).withTestCaseId(testCase.getId()).withStatus(resultStatus.getString());

		AutomationTestLogRequest automationTestLogRequest = new AutomationTestLogRequest()
				.withProjectId(_project.getId()).withTestRunId(testRun.getId())
				.withAutomationTestLog(automationTestLog);

		// TestLog testLog =
		_testExecutionService.submitAutomationTestLog(automationTestLogRequest);
	}

}
