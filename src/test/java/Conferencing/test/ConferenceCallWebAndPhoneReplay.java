package Conferencing.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.OnlineConfirmationPage;
import Pages.ParticipantsDashboardPage;
import Pages.CCReplay.*;
import Pages.DeclarationPage;
import Pages.DisclaimerPage;
import Pages.EmailEntryPage;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.ConferenceCall;
import Pages.ShowManagement.Conferencing;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.NRSHome;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class ConferenceCallWebAndPhoneReplay extends Base {

	private String automationShowName = "AutoConfCall61" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String confCallName01 = "ConfCall01" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private Calendar currentDate;
	String timeStamp;
	String[] investorUrl;

	public ConferenceCallWebAndPhoneReplay() {
		testRunType = TestRunType.Smoke;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify Conference Call Only for Web and Phone with Registration set to Yes", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyConferenceCallOnlyForWebAndPhoneWithRegistration() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		navigateToShowManagement(testDataDto, userName, password);
		SMHome smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.clickCreateNew();

		ShowDetails showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.enterUnderwriter(testDataHelper.getValue("Underwriter"));
		showdetails.enterunderwriterRole(testDataHelper.getValue("UnderwriterRole"));
		showdetails.clickAdd();
		showdetails.selectMediaType(ShowType.ConferenceCall.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		logger.log("Show Name : " + automationShowName);
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-1));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();
		Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.clickSaveAndContinue();
		Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.selectDisclaimer(testDataHelper.getValue("Disclaimer"));
		disclaimer.clickAdd();
		disclaimer.clickSaveAndContinue();
		EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.enterEntryCode(entryCode);
		Thread.sleep(1000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.clickContinue();
		ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
		confCall.setDependencies(logger, testDataHelper);
		confCall.enterConfName(confCallName);
		confCall.selectUnderwriterName(testDataHelper.getValue("Underwriter"));
		confCall.selectAttendees(testDataHelper.getValue("Attendees"));
		confCall.enableRegistration();
		confCall.selectReplayMode("Web and Phone");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(-35));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(-1));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		Thread.sleep(3000);
		
		connectFNRegistrationLink = confCall.clickAndCopyRegistrationLink();
		logger.log("ConnectFN Link : " + connectFNRegistrationLink);		
		confCall.clickOkayButton();
		Thread.sleep(3000);
		confCall.clickContinue();
		Thread.sleep(5000);
		Billing billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.clickSaveAndContinue();
		Thread.sleep(5000);
		Review review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.clickMakeShowLive();
		review.clickYes();
		Thread.sleep(3000);
	}

	@Test(description = "Login to CC Replay and Upload the Files", enabled = true, dependsOnMethods = {"verifyConferenceCallOnlyForWebAndPhoneWithRegistration"})
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void loginToCCReplayAndUploadFiles() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		
		navigateToURL(testDataDto, testDataHelper.getValue("CCReplayURL"));
		CCReplayLoginPage ccReplayLogin = PageFactory.initElements(driver, CCReplayLoginPage.class);
		ccReplayLogin.setDependencies(logger, testDataHelper);
		ccReplayLogin.enterUserNameCCLoginPage(testDataHelper.getValue("ReplayUsername"));
		ccReplayLogin.enterPasswordCCLoginPage(testDataHelper.getValue("ReplayPassword"));
		ccReplayLogin.clickLogin();
		
		CCReplayConferenceCallsPage ccReplayCCPage = PageFactory.initElements(driver, CCReplayConferenceCallsPage.class);
		ccReplayCCPage.setDependencies(logger, testDataHelper);		
		ccReplayCCPage.clickReplayConferenceCall();
		ccReplayCCPage.clickNewCallsButton();
		Thread.sleep(5000);
		ccReplayCCPage.clickReplayCall(confCallName);
		Thread.sleep(4000);
		ccReplayCCPage.selectCustomerName("Dan's Bank");
		ccReplayCCPage.verifyCallType("Web and Phone");
		ccReplayCCPage.enterWavFile("Small file.wav");
		ccReplayCCPage.clickNamePrompt();
		ccReplayCCPage.forwardButton();
		Thread.sleep(4000);
		ccReplayCCPage.clickSaveButton();
		Thread.sleep(4000);
		ccReplayCCPage.verifyAlertErrorMessage();
		ccReplayCCPage.verifyUpdateButton();
	}
		
		@Test(description = "Verify replay on call confirmation page", enabled = true, dependsOnMethods = {"loginToCCReplayAndUploadFiles"})
		@CustomTestAnnotations(testCaseNumber = "TC-112")
		public void verifyReplayOnCallConfirmationPage() throws Throwable, Exception {
			CommonTestDataDto testDataDto = commonTestData.get();
			RemoteWebDriver driver = testDataDto.getDriver();
			Log4JLogger logger = testDataDto.getLogger();
			TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
			driver.get(connectFNRegistrationLink);
			Thread.sleep(5000);
			EmailEntryPage emailEntryPage = PageFactory.initElements(driver, EmailEntryPage.class);
			emailEntryPage.setDependencies(logger, testDataHelper);
			emailEntryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail"));
			emailEntryPage.clickContinue();
			Thread.sleep(5000);
			DisclaimerPage disclaimerPage = PageFactory.initElements(driver, DisclaimerPage.class);
			disclaimerPage.setDependencies(logger, testDataHelper);
			disclaimerPage.verifyDisclaimerPage();
			disclaimerPage.clickAccept();
			Thread.sleep(5000);
			OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
			dailInDetails.setDependencies(logger, testDataHelper);
			dailInDetails.verifyDialInPage();
			Thread.sleep(4000);
			dailInDetails.verifyreplayMessage();
			dailInDetails.verifyreplayPlayIcon();
			Thread.sleep(2000);
			dailInDetails.clickreplayPlayIcon();
			currentDate=Calendar.getInstance();
			timeStamp = ComponentBase.modTime(0, currentDate, new SimpleDateFormat("h:mm a"),currentDate.getTimeZone().getID().toString());
			System.out.println("Replay Time is:" +timeStamp);
			Thread.sleep(4000);
		}
		
				
		@Test(description = "Verify replay on MYNRS Conference Call page & Live dashboard", enabled = true, dependsOnMethods = {"verifyReplayOnCallConfirmationPage"})
		@CustomTestAnnotations(testCaseNumber = "TC-104")
		public void verifyReplayOnMyNRSConferenceCallPage() throws Throwable, Exception {
			CommonTestDataDto testDataDto = commonTestData.get();
			RemoteWebDriver driver = testDataDto.getDriver();
			Log4JLogger logger = testDataDto.getLogger();
			TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
			NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
			nrsLogin.setDependencies(logger, testDataHelper);
			nrsLogin.loginSM(testDataHelper.getValue("SMURL"), userName, password, testDataDto);
			NRSHome nrsHome = PageFactory.initElements(driver, NRSHome.class);
			nrsHome.setDependencies(logger, testDataHelper);
			nrsHome.clickConferencing();
			Conferencing conferencing = PageFactory.initElements(driver, Conferencing.class);
			conferencing.setDependencies(logger, testDataHelper);
			conferencing.enterSearchText(automationShowName);
			conferencing.clickConferenceName();
			Thread.sleep(3000);
			conferencing.verifyReplayPlayicon();
			conferencing.verifyReplayStatus("Replay Available Until");
			Thread.sleep(3000);
			conferencing.launchLiveDashboard();
			ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver, ParticipantsDashboardPage.class);
			participantsDashboardPage.setDependencies(logger, testDataHelper);
			participantsDashboardPage.verifyPageTitle("Conference Dashboard");
			Thread.sleep(4000);
			participantsDashboardPage.verifyDate(ComponentBase.modDate(0, currentDate, new SimpleDateFormat("MM/dd/YY"),currentDate.getTimeZone().toString()));		
			String time=participantsDashboardPage.verifyTime(testDataHelper.getValue("RegisteredUserEmail"));
			participantsDashboardPage.compareTime(timeStamp, time); 
	  }
		
		
		@Test(description = "Verify CC only call edit to replay set to yes", enabled = true, dependsOnMethods={"verifyConferenceCallOnlyForWebAndPhoneWithRegistration"}, priority=1)
		@CustomTestAnnotations(testCaseNumber = "TC-127")
		public void editCCOnlyCall() throws Throwable, Exception {
			CommonTestDataDto testDataDto = commonTestData.get();
			RemoteWebDriver driver = testDataDto.getDriver();
			Log4JLogger logger = testDataDto.getLogger();
			TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
			// Login and Create a new Conference Call on NRS
			navigateToShowManagement(testDataDto, userName, password);
			SMHome smHome = PageFactory.initElements(driver, SMHome.class);
			smHome.setDependencies(logger, testDataHelper);
			smHome.enterSearchTextSMGrid(automationShowName);
			smHome.clickShowName(automationShowName);
		
			ShowDetails showdetails = PageFactory.initElements(driver, ShowDetails.class);
			showdetails.setDependencies(logger, testDataHelper);
			showdetails.clickSaveAndContinue();
			
			Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
			documentaion.setDependencies(logger, testDataHelper);
			documentaion.clickSaveAndContinue();
			
			Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
			disclaimer.setDependencies(logger, testDataHelper);
			disclaimer.clickSaveAndContinue();
			
			EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
			entryCodes.setDependencies(logger, testDataHelper);
			entryCodes.clickContinue();
			
			ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
			confCall.setDependencies(logger, testDataHelper);
			confCall.enterConfName(confCallName01);
			confCall.selectUnderwriterName(testDataHelper.getValue("Underwriter"));
			confCall.selectAttendees(testDataHelper.getValue("Attendees"));			
			confCall.disableRegistration();
			confCall.selectReplayMode("Phone Only");
			confCall.enterStartDate(ComponentBase.modDate(0));
			confCall.enterStartTime(ComponentBase.modTime(-35));
			confCall.enterEndDate(ComponentBase.modDate(0));
			confCall.enterEndTime(ComponentBase.modTime(-1));
			confCall.clickAndAddEntryCode(entryCode);			
			confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
			Thread.sleep(5000);			
			confCall.clickAdd();
			Thread.sleep(3000);
			confCall.clickContinue();
			Thread.sleep(5000);
				
		}
		
		@Test(description = "Login to CC Replay and Upload the Files", enabled = true, dependsOnMethods = {"editCCOnlyCall"})
		@CustomTestAnnotations(testCaseNumber = "TC-104")
		public void loginToCCReplayAndUploadPhoneOnlyFiles() throws Throwable, Exception {
			CommonTestDataDto testDataDto = commonTestData.get();
			RemoteWebDriver driver = testDataDto.getDriver();
			Log4JLogger logger = testDataDto.getLogger();
			TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
			
			navigateToURL(testDataDto, testDataHelper.getValue("CCReplayURL"));
			CCReplayLoginPage ccReplayLogin = PageFactory.initElements(driver, CCReplayLoginPage.class);
			ccReplayLogin.setDependencies(logger, testDataHelper);
			ccReplayLogin.enterUserNameCCLoginPage(testDataHelper.getValue("ReplayUsername"));
			ccReplayLogin.enterPasswordCCLoginPage(testDataHelper.getValue("ReplayPassword"));
			ccReplayLogin.clickLogin();
			
			CCReplayConferenceCallsPage ccReplayCCPage = PageFactory.initElements(driver, CCReplayConferenceCallsPage.class);
			ccReplayCCPage.setDependencies(logger, testDataHelper);		
			ccReplayCCPage.clickReplayConferenceCall();
			ccReplayCCPage.clickNewCallsButton();
			Thread.sleep(5000);
			ccReplayCCPage.clickReplayCall(confCallName01);
			Thread.sleep(4000);
			ccReplayCCPage.selectCustomerName("Dan's Bank");
			ccReplayCCPage.verifyCallType("Phone Only");
			ccReplayCCPage.enterWavFile("Small file.wav");
			ccReplayCCPage.clickNamePrompt();
			ccReplayCCPage.forwardButton();
			Thread.sleep(4000);
			ccReplayCCPage.clickSaveButton();
			Thread.sleep(4000);
			ccReplayCCPage.verifyAlertErrorMessage();
			ccReplayCCPage.verifyUpdateButton();
		}
}
