package Conferencing.test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import org.testng.annotations.Test;

import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.CallStatus;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.ConferenceCallEndedPage;
import Pages.ParticipantsDashboardPage;
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

public class CallStatusWhenCallHasEnded extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConnectFNCall" + ComponentBase.dateTime(); 
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String conferencingRegistrationLink = "";
	String callPageURL;
	String conferencingURL;

	public CallStatusWhenCallHasEnded() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create an already Ended Call", enabled = true, priority=0)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createEndedCall() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS

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
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();

		Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.clickShare();
		documentaion.enterSharedUserEmailAddress(testDataHelper.getValue("RegularAccount"));
		documentaion.clickAddButtonShareModal();
		documentaion.clickShareButtonModal();
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
		confCall.enterStartDate(ComponentBase.modDate(-2));
		confCall.enterStartTime(ComponentBase.modTime(10));
		confCall.enterEndDate(ComponentBase.modDate(-2));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		callPageURL = driver.getCurrentUrl();

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
		driver.get(callPageURL);

		conferencingRegistrationLink = confCall.clickAndCopyRegistrationLink();
		logger.log("conferencingRegistrationLink: " + conferencingRegistrationLink);
	}

    @Test(description = "Verify call status on live dashboard when call has ended", enabled = true, dependsOnMethods={"createEndedCall"},priority=2)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void verifyCallEndedStatusOnLiveDashboard() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMHOMEURL"),  userName, password, testDataDto);
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
		confCall.clickToOpenLiveDashboard();
		Thread.sleep(10000);
		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,ParticipantsDashboardPage.class); 
		participantsDashboardPage.setDependencies(logger,testDataHelper); 
		Thread.sleep(5000);
	//	participantsDashboardPage.verifyDownloadFinalReport();
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Call_Expired.getCallstatus());				
	}

	@Test(description = "Verify call status on dial in page when call has ended", enabled = true, dependsOnMethods={"createEndedCall"},priority=1)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void verifyCallEndedStatusOnDialIn() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, conferencingRegistrationLink);
		Thread.sleep(4000);		
		ConferenceCallEndedPage conferenceCallEndedPage = PageFactory.initElements(driver, ConferenceCallEndedPage.class);
		conferenceCallEndedPage.setDependencies(logger, testDataHelper);
		conferenceCallEndedPage.verifyConferenceEndedPageHeading();
		conferenceCallEndedPage.verifyConferenceEndedPageTextBody();
	}

	@Test(description = "Verify replay state on right panel both active and conference call tab", enabled = true, dependsOnMethods={"createEndedCall"},priority=3)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void verifyReplayStatusOnMyNrs() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(testDataHelper.getValue("SMURL"), testDataHelper.getValue("RegularAccount"), testDataHelper.getValue("RegularAccountPassword"), testDataDto );


		NRSHome nRSHome = PageFactory.initElements(driver, NRSHome.class);
		nRSHome.setDependencies(logger, testDataHelper);
		nRSHome.enterEntryCode(entryCode);
		nRSHome.clickAddEntryCode();
		nRSHome.clickShowName(automationShowName);
		nRSHome.VerifyCallStatus(CallStatus.Call_Expired.getCallstatus());
		nRSHome.VerifyglobalDialInLink(true);
		nRSHome.clickAddToFavouities(automationShowName);
		nRSHome.clickFavouitiesTab();
		nRSHome.clickShowName(automationShowName);
		nRSHome.clickAddToFavouities(automationShowName);
		
		
		nRSHome.clickConferencing();

		Conferencing conferencing = PageFactory.initElements(driver, Conferencing.class);
		conferencing.setDependencies(logger, testDataHelper);
		conferencing.enterSearchText(automationShowName);
		conferencing.clickConferenceName();
		conferencing.VerifyCallStatus(CallStatus.Call_Expired.getCallstatus());
		conferencing.verifyReplayStatus("Not Requested");
		conferencing.VerifyglobalDialInLink(true);
		conferencingURL = driver.getCurrentUrl();
		nRSHome.clickLogout();
		
	}

	@Test(description = "Verify changing replay state on right panel both active and conference call tab", enabled = true, dependsOnMethods={"verifyReplayStatusOnMyNrs"},priority=4)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void changeReplayStatus() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);

		nRSLogin.loginSM(testDataHelper.getValue("SMURL"), userName, password, testDataDto );

		driver.get(callPageURL);
		ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
		confCall.setDependencies(logger, testDataHelper);
		confCall.clickEditCCCall();
		confCall.selectReplayMode("Web Only");
		Thread.sleep(5000);
		confCall.clickUpdate();

	}
	
	@Test(description = "Verify changing replay state on right panel both active and conference call tab", enabled = true, dependsOnMethods={"changeReplayStatus"},priority=5)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void verifyReplayStatusOnMyNrsAfterStatechange() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(testDataHelper.getValue("SMURL"), testDataHelper.getValue("RegularAccount"), testDataHelper.getValue("RegularAccountPassword"), testDataDto );


		NRSHome nRSHome = PageFactory.initElements(driver, NRSHome.class);
		nRSHome.setDependencies(logger, testDataHelper);
		nRSHome.clickShowName(automationShowName);
		nRSHome.VerifyCallStatus(CallStatus.Call_Ended.getCallstatus());
		nRSHome.VerifyglobalDialInLink(true);
		nRSHome.clickConferencing();

		Conferencing conferencing = PageFactory.initElements(driver, Conferencing.class);
		conferencing.setDependencies(logger, testDataHelper);
		conferencing.enterSearchText(automationShowName);
		conferencing.clickConferenceName();
		conferencing.VerifyCallStatus(CallStatus.Call_Ended.getCallstatus());
		conferencing.verifyReplayStatus("Available Soon");
		conferencing.VerifyglobalDialInLink(true);

	}
}
