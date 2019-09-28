package Conferencing.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.JavascriptExecutor;
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

public class ReplayExpiration extends Base {

	private String automationShowName = "AutoConfCall81" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String confCallName01 = "ConfCall02" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String entryCode01 = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private String connectFNRegistrationLink01 = "";
	String dialInURL;
	String[] investorUrl;

	public ReplayExpiration() {
		testRunType = TestRunType.Smoke;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify replay expiration on call confirmation page ", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyReplayExpirationOnCallConfirmationPage() throws Throwable, Exception {
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
		confCall.selectReplayMode("Web Only");
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
	
	@Test(description = "Login to CC Replay and Upload the Files", enabled = true, dependsOnMethods = {"verifyReplayExpirationOnCallConfirmationPage"})
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
		ccReplayCCPage.verifyCallType("Web Only");
		ccReplayCCPage.enterWavFile("Small file.wav");
		Thread.sleep(4000);
		ccReplayCCPage.clickSaveButton();
		Thread.sleep(4000);
		ccReplayCCPage.verifyAlertErrorMessage();
		ccReplayCCPage.verifyUpdateButton();
	}
		
		@Test(description = "Verify replay on call confirmation page", enabled = true, dependsOnMethods = {"loginToCCReplayAndUploadFiles"})
		@CustomTestAnnotations(testCaseNumber = "TC-112")
		public void verifyReplayOnConfirmationPage() throws Throwable, Exception {
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
			dialInURL= dailInDetails.fetchingURLCallConfirmationPage();			
			Thread.sleep(2000);
		}
		
		@Test(description = "Edit the conference call, expire the replay and verify the call ended page", enabled = true, dependsOnMethods={"verifyReplayOnConfirmationPage"})
		@CustomTestAnnotations(testCaseNumber = "TC-127")
		public void editCCOnlyCallAndVerifyCallEndedPage() throws Throwable, Exception {
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
			entryCodes.waitForLoadingSM();
			Thread.sleep(4000);
			
			ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
			confCall.setDependencies(logger, testDataHelper);
			confCall.clickEditCCCall();
			Thread.sleep(3000);
			confCall.enterReplayEndDate(ComponentBase.modDate(-5));
			Thread.sleep(4000);
			confCall.clickUpdate();			
			driver.get(dialInURL);
			Thread.sleep(5000);
			
			OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
			dailInDetails.setDependencies(logger, testDataHelper);
			dailInDetails.verifyDialInPage();
			Thread.sleep(4000);			
			dailInDetails.verifyDialInPageCallEndedPage();	
		
		}
				
		@Test(description = "Create conference call when replay is expired and verify call ended page", enabled = true, dependsOnMethods={"verifyReplayExpirationOnCallConfirmationPage"}, priority=1)
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
			entryCodes.enterEntryCode(entryCode01);
			Thread.sleep(1000);
			entryCodes.clickAdd();
			Thread.sleep(2000);
			entryCodes.clickContinue();
			
			ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
			confCall.setDependencies(logger, testDataHelper);
			confCall.enterConfName(confCallName01);
			confCall.selectUnderwriterName(testDataHelper.getValue("Underwriter"));
			confCall.selectAttendees(testDataHelper.getValue("Attendees"));
			confCall.enableRegistration();
			confCall.selectReplayMode("Web Only");		
			confCall.enterReplayEndDate(ComponentBase.modDate(-5));			
			confCall.enterStartDate(ComponentBase.modDate(0));
			confCall.enterStartTime(ComponentBase.modTime(-35));
			confCall.enterEndDate(ComponentBase.modDate(0));
			confCall.enterEndTime(ComponentBase.modTime(-1));
			confCall.clickAndAddEntryCode(entryCode01);			
			confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
			Thread.sleep(5000);
			confCall.clickAdd();
			Thread.sleep(3000);
			
			connectFNRegistrationLink01 = confCall.registrationLinkWhenMultipleCalls(confCallName01);
			logger.log("ConnectFN Link : " + connectFNRegistrationLink01);
			Thread.sleep(3000);
			
			driver.get(connectFNRegistrationLink01);
			Thread.sleep(5000);
			OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
			dailInDetails.setDependencies(logger, testDataHelper);
			dailInDetails.verifyDialInPage();
			Thread.sleep(4000);		
			dailInDetails.verifyDialInPageCallEndedPage();
							
		}
}

