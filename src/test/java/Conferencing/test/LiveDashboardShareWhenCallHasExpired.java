package Conferencing.test;

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
import Miscellaneous.Enums.CallStatus;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.ConferenceDashboardGuestLoginPage;
import Pages.OnlineConfirmationPage;
import Pages.ParticipantsDashboardPage;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.ConferenceCall;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.NRSHome;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class LiveDashboardShareWhenCallHasExpired extends Base{
	private String automationShowName = "AutoSMCCShared" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "CCCallExpired" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String conferencingRegistrationLink = "";
	String callPageURL = "";
	String LDLink = "";

	public LiveDashboardShareWhenCallHasExpired() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}
	
	@Test(description = "Verify Conference Call Creation No Disclaimer and No Privacy Declaration When Call Ended", enabled = true)
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
		showdetails.enterStartDate(ComponentBase.modDate(-15));
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
		confCall.enterStartDate(ComponentBase.modDate(-12));
		confCall.enterStartTime(ComponentBase.modTime(-35));
		confCall.enterEndDate(ComponentBase.modDate(-12));
		confCall.enterEndTime(ComponentBase.modTime(5));
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
		driver.get(conferencingRegistrationLink);
		Thread.sleep(5000);
		/*EmailEntryPage emailEntryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		emailEntryPage.setDependencies(logger, testDataHelper);
		emailEntryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail"));
		emailEntryPage.clickContinue();*/

		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPageCallEndedPage();
	}
	
	@Test(description = "Verify shared user is able to revoke and restore access on LD", enabled = true, dependsOnMethods={"creatCCOnlyCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-127")
	public void sharedUserRovokeRestoreLD() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(callPageURL, testDataHelper.getValue("RegularAccount"), testDataHelper.getValue("RegularAccountPassword"),testDataDto );

		ConferenceCall confCall = PageFactory.initElements(driver, ConferenceCall.class);
		confCall.setDependencies(logger, testDataHelper);

		confCall.clickToOpenLiveDashboard();
		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);

		//participantsDashboardPage.verifyRegisteredUser(testDataHelper.getValue("RegisteredUserEmail"));
		participantsDashboardPage.verifyPageTitle("Conference Dashboard");
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Call_Expired.getCallstatus());

	}

	@Test(description = "Verify LD shared user  is able to revoke and restore access on LD", enabled = true, dependsOnMethods={"creatCCOnlyCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-127")
	public void shareLDFromMyNrsAndUsertryRevokeRestore() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(testDataHelper.getValue("SMURL"), userName, password, testDataDto );


		NRSHome nRSHome = PageFactory.initElements(driver, NRSHome.class);
		nRSHome.setDependencies(logger, testDataHelper);
		nRSHome.enterEntryCode(entryCode);
		nRSHome.clickAddEntryCode();
		Thread.sleep(5000);
		nRSHome.clickShowName(automationShowName);
		Thread.sleep(5000);
		nRSHome.clickShareDashboard();
		nRSHome.enterEmail(testDataHelper.getValue("RegularAccount2"));
		nRSHome.clickShare();
		//nRSHome.clickLogout();

		LDLink = nRSHome.getLiveDashboardLinkMailtrap(automationShowName,testDataHelper.getValue("RegularAccount2"));
		System.out.println(LDLink);

		driver.get(LDLink);
		ConferenceDashboardGuestLoginPage conferenceDashboardGuestLoginPage = PageFactory.initElements(driver, ConferenceDashboardGuestLoginPage.class);
		conferenceDashboardGuestLoginPage.setDependencies(logger, testDataHelper);

		conferenceDashboardGuestLoginPage.enterFirstName("firstName");
		conferenceDashboardGuestLoginPage.enterLastName("lastName");
		conferenceDashboardGuestLoginPage.enterEmail(testDataHelper.getValue("RegularAccount2"));
		conferenceDashboardGuestLoginPage.enterCompany("company");
		conferenceDashboardGuestLoginPage.clickViewDashboard();

		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		Thread.sleep(5000);
		//participantsDashboardPage.verifyRegisteredUser(testDataHelper.getValue("RegisteredUserEmail"));
		participantsDashboardPage.verifyPageTitle("Conference Dashboard");
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Call_Expired.getCallstatus());


	}

}
