package Conferencing.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.Configuration;
import Miscellaneous.CustomTestAnnotations;
import Miscellaneous.Log4JLogger;
import Miscellaneous.TestDataHelper;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.OnlineConfirmationPage;
import Pages.ParticipantsDashboardPage;
import Pages.CCReplay.*;
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
import junit.framework.Assert;

public class ConferenceNavigatesFromCallLiveToCallEnded extends Base  {
	
	private String automationShowName = "AutoSMCCCallReplay" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConnectFNCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private Calendar currentDate;
	String timeStamp;
	String callPageURL = "";
	
	public ConferenceNavigatesFromCallLiveToCallEnded() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}
	
	@Test(description = "Verify ConnectFN Conference Call Creation No Disclaimer and No Privacy Declaration", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-127")
	public void creatCCOnlyCall() throws Throwable, Exception {
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
		confCall.selectReplayMode("None");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(-35));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(3));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		connectFNRegistrationLink = confCall.clickAndCopyRegistrationLink();
		logger.log("ConnectFN Link : " + connectFNRegistrationLink);
		confCall.clickOkayButton();
		Thread.sleep(4000);
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
		
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage emailEntryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		emailEntryPage.setDependencies(logger, testDataHelper);
		emailEntryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail"));
		emailEntryPage.clickContinue();
		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPage();
		onlineConfirmationPage.verifyDialInPageMoveToCallEndedPage();
	}
	
	
	@Test(description = "Verify CC only call edit to replay set to yes", enabled = true, dependsOnMethods={"creatCCOnlyCall"})
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
		confCall.clickEditCCCall();
		confCall.selectReplayMode("Web Only");
		Thread.sleep(5000);
		confCall.clickUpdate();
		
		callPageURL = driver.getCurrentUrl();
		
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage emailEntryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		emailEntryPage.setDependencies(logger, testDataHelper);
		emailEntryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail2"));
		emailEntryPage.clickContinue();
		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPageMoveToCallReplayPage();	
	
	}
	
	@Test(description = "Verify user on LD CC only", enabled = true, dependsOnMethods={"editCCOnlyCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-127")
	public void verifyUserCCOnlyLD() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(callPageURL, userName, password,testDataDto );
		
		ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
		confCall.setDependencies(logger, testDataHelper);
		confCall.clickToOpenLiveDashboard();
		
		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver, ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		participantsDashboardPage.verifyRegisteredUser(testDataHelper.getValue("RegisteredUserEmail2"));
	
	}
	
	@Test(description = "Login to CC Replay and Upload the Files", enabled = true, dependsOnMethods = {"editCCOnlyCall"})
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
	public void verifyReplayOnCallConfirmationPage() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage emailEntryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		emailEntryPage.setDependencies(logger, testDataHelper);
		emailEntryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail2"));
		emailEntryPage.clickContinue();
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
		String time=participantsDashboardPage.verifyTime(testDataHelper.getValue("RegisteredUserEmail2"));
		participantsDashboardPage.compareTime(timeStamp, time);   	      
	}
  }
