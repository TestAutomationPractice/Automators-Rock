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
import Pages.ShowManagement.ConferenceCallProfiles;
import Pages.ShowManagement.Maintenance;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.NewConferenceCallProfileForm;

public class VerifyConferenceCallProfile extends Base {

	private static String automationCCProfileName = "AutoProfile" + ComponentBase.dateTime();
	private String errorMessagDefaultCountry = "There must be a at least one (1) Speaker Number (either Toll or Toll-Free or both) and at least one (1) Operator Number (either Toll or Toll-Free or both) associated to Profile’s Default Country.";
	private String errorMessageUnitedStates = "There must be a Speaker Toll-Free Number and Operator Assisted Toll-Free Number associated to United States";
	private String errorMessageOtherCountry = "There must be at least one (1) number (Speaker, Speaker Toll-Free , Operator Assisted or Operator Assisted Toll-Free) associated with a country other than the US or the Profile’s Default Country.";
	
	public VerifyConferenceCallProfile() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create an ConferenceCallProfile and test various Error messages and checks", enabled = true)
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
		maintenance.clickConferenceCallProfiles();
		
		ConferenceCallProfiles confCallProfile = PageFactory.initElements(driver, ConferenceCallProfiles.class);
		confCallProfile.setDependencies(logger, testDataHelper);
		confCallProfile.verifyPageLabel();
		confCallProfile.verifyFilters();
		confCallProfile.clickNewConferenceCallProfile();
		Thread.sleep(3000);
		
		NewConferenceCallProfileForm newCCProfile = PageFactory.initElements(driver, NewConferenceCallProfileForm.class);
		newCCProfile.setDependencies(logger, testDataHelper);
		
		newCCProfile.clickCancel();
		Thread.sleep(2000);
		
		confCallProfile.verifyPageLabel();
		confCallProfile.verifyFilters();
		confCallProfile.clickNewConferenceCallProfile();
		
		Thread.sleep(3000);
		newCCProfile.enterProfileName(automationCCProfileName);
		newCCProfile.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage("Please select a value for default country field.");
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		newCCProfile.selectDefaultCountry(testDataHelper.getValue("DefaultCountry"));
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage("Default Country and phone number details needs to be added.");
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		newCCProfile.clickAdd();
		newCCProfile.selectCountry(testDataHelper.getValue("DefaultCountry"));
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage(errorMessagDefaultCountry);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		
		newCCProfile.clickCancel();
		newCCProfile.clickPopUpCancel();
		Thread.sleep(2000);
		
		int rowID = 2;
		newCCProfile.enterOperatorAssistedNumber(generateNumInStringFormat(10),rowID);
		newCCProfile.enterSpeakerNumber(generateNumInStringFormat(10),rowID);
		newCCProfile.clickSave();
		String errorMsg = "The Speaker Number,Operator Assisted Phone Number for " + testDataHelper.getValue("DefaultCountry") +" is not of right length. Please enter a valid phone number.";
		newCCProfile.verifyPopUpMessage(errorMsg);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		newCCProfile.enterSpeakerTollFreeNumber(generateNumInStringFormat(8), rowID);
		newCCProfile.enterSpeakerNumber(generateNumInStringFormat(8), rowID);
		newCCProfile.clickSave();
		errorMsg = "The Operator Assisted Phone Number for " + testDataHelper.getValue("DefaultCountry") +" is not of right length. Please enter a valid phone number.";
		newCCProfile.verifyPopUpMessage(errorMsg);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		newCCProfile.enterOperatorAssistedNumber(generateNumInStringFormat(8),rowID);
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage(errorMessageUnitedStates);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		
		rowID = 1;
		newCCProfile.enterSpeakerTollFreeNumber(generateNumInStringFormat(10), rowID);
		newCCProfile.enterSpeakerNumber(generateNumInStringFormat(10), rowID);
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage(errorMessageUnitedStates);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		errorMsg = "The Operator Assisted Toll-Free Phone Number for United States is not of right length. Please enter a valid phone number.";
		newCCProfile.enterOperatorAssistedTollFreeNumber(generateNumInStringFormat(5), rowID);
		newCCProfile.clickSave();
		newCCProfile.verifyPopUpMessage(errorMsg);
		Thread.sleep(3000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		newCCProfile.enterOperatorAssistedTollFreeNumber(generateNumInStringFormat(10), rowID);
		newCCProfile.clickSave();
		
		errorMsg = "There there are no Automated numbers listed for " + testDataHelper.getValue("DefaultCountry") + ". Please add Automated Numbers for " + testDataHelper.getValue("DefaultCountry")+ " to the Automated Phone Directory.";
		newCCProfile.verifyPopUpMessage(errorMsg);
		Thread.sleep(3000);
		newCCProfile.clickPopUpClose();
		
		newCCProfile.verifySuccessfulMessage();
		newCCProfile.clickAdd();
		newCCProfile.selectCountry(testDataHelper.getValue("OtherCountry"));
		newCCProfile.clickUpdate();
		newCCProfile.verifyPopUpMessage(errorMessageOtherCountry);
		Thread.sleep(2000);
		newCCProfile.clickPopUpClose();
		Thread.sleep(2000);
		
		rowID = 2;
		newCCProfile.enterOperatorAssistedTollFreeNumber(generateNumInStringFormat(10), rowID);
		newCCProfile.clickUpdate();
		newCCProfile.verifyPopUpMessage(errorMsg);
		Thread.sleep(3000);
		newCCProfile.clickPopUpClose();
		
		newCCProfile.verifySuccessfulMessage();
		Thread.sleep(2000);
		newCCProfile.clickCancel();
		
		Thread.sleep(2000);
		confCallProfile.verifyPageLabel();
		confCallProfile.verifyFilters();
		
		confCallProfile.enterFilterProfile(automationCCProfileName);
		Thread.sleep(2000);
		confCallProfile.deleteProfile();
		
	}
}