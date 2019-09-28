package Maintenance.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.Test;
import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.ShowManagement.AutomatedPhoneDirectory;
import Pages.ShowManagement.ConferenceCallProfiles;
import Pages.ShowManagement.Maintenance;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.NewConferenceCallProfileForm;

public class VerifyAutomatedPhoneDirectory extends Base {

	private String errorMessageCountry = "Please select Country.";
	private String errorMessage = "Country Algeria must have atleast one phone number.";
	private String country = "Algeria";
	
	public VerifyAutomatedPhoneDirectory() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Open Automated Phone Directory and test various Error messages and checks", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createCCProfile() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		
		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMMaintenanceURL"), userName, password, testDataDto);
		
		Maintenance maintenance =  PageFactory.initElements(driver, Maintenance.class);
		maintenance.setDependencies(logger, testDataHelper);
		maintenance.clickAutomatedPhoneDirectory();
		Thread.sleep(2000);
		
		AutomatedPhoneDirectory automatedPhoneDirectory = PageFactory.initElements(driver, AutomatedPhoneDirectory.class);
		automatedPhoneDirectory.setDependencies(logger, testDataHelper);
		Thread.sleep(2000);
		automatedPhoneDirectory.verifyHeaderElements();
		
		automatedPhoneDirectory.clickAdd();
		
		automatedPhoneDirectory.clickCancel();
		Thread.sleep(2000);
		automatedPhoneDirectory.clickPopUpCancel();
		Thread.sleep(2000);
		
		automatedPhoneDirectory.clickSave();
		automatedPhoneDirectory.verifyPopUpMessage(errorMessageCountry);
		Thread.sleep(2000);
		automatedPhoneDirectory.clickPopUpClose();
		Thread.sleep(2000);
		
		/*int rowID = 1;
		//automatedPhoneDirectory.enterAutomatedNumber(generateNumInStringFormat(18), rowID);
		automatedPhoneDirectory.selectCountry(country);
		
		automatedPhoneDirectory.clickSave();
		automatedPhoneDirectory.verifyPopUpMessage(errorMessage);
		Thread.sleep(2000);
		automatedPhoneDirectory.clickPopUpClose();
		Thread.sleep(2000);*/
		
		automatedPhoneDirectory.clickCancel();
		automatedPhoneDirectory.clickPopUpDontSave();
		Thread.sleep(4000);
	}
}