package Production.test;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import Conferencing.Test.Base;
import Conferencing.Test.ComponentBase;
import DTO.CommonTestDataDto;
import Miscellaneous.*;
import Miscellaneous.Enums.CallStatus;
import Miscellaneous.Enums.ShowStatus;
import Miscellaneous.Enums.ShowStatusColor;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.OnlineConfirmationPage;
import Pages.ParticipantsDashboardPage;
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

public class ConfereceCallOnly_UserReg extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCallProd" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	String[] investorUrl;

	public ConfereceCallOnly_UserReg() {
		testRunType = TestRunType.Smoke;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify ConnectFN Conference Call Creation with Disclaimer and Enable Public Private", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void creatConfCallWithDisclaimerPrivacyDeclaration() throws Throwable, Exception {
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
		confCall.enableLiveStreaming();
		confCall.selectReplayMode("None");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(0));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.enablePrivatePublic();
		confCall.enableSalesRep();
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		connectFNRegistrationLink = confCall.clickAndCopyRegistrationLink();
		logger.log("ConnectFN Link : " + connectFNRegistrationLink);
		confCall.clickContinue();
		Thread.sleep(5000);
		Billing billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.clickSaveAndContinue();
		Thread.sleep(5000);
		Review review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.verifyShowStatus(ShowStatus.Disabled.getString());
		review.verifyShowColor(ShowStatusColor.Red.getString());
		review.clickMakeShowLive();
		review.clickYes();
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());
		Thread.sleep(3000);
	}

	@Test(description = "Verify User Registration with one Disclaimer and Private Public and Sales Rep Enabled", enabled = true, dependsOnMethods = {"creatConfCallWithDisclaimerPrivacyDeclaration"})
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyUserRegistrationDisclaimerDeclarationEnabled() throws Throwable, Exception {
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
		DeclarationPage declarationPage = PageFactory.initElements(driver, DeclarationPage.class);
		declarationPage.setDependencies(logger, testDataHelper);
		declarationPage.selectPublic();
		declarationPage.enterSalesRepName("TestSales");
		declarationPage.clickContinue();
		Thread.sleep(5000);
		DisclaimerPage disclaimerPage = PageFactory.initElements(driver, DisclaimerPage.class);
		disclaimerPage.setDependencies(logger, testDataHelper);
		disclaimerPage.verifyDisclaimerPage();
		disclaimerPage.clickAccept();
		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPage();
		onlineConfirmationPage.verifyDailInTabExists();
		onlineConfirmationPage.verifyLiveStreamTabExists();
		onlineConfirmationPage.verifyDailInNumbers(testDataHelper.getValue("UnitedStatesTollFree"), testDataHelper.getValue("UnitedStatesLocal"));
		Thread.sleep(5000);
	}

	@Test(description = "Verify Call on Conference Call page", enabled = true, dependsOnMethods = {"verifyUserRegistrationDisclaimerDeclarationEnabled"})
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyCall() throws Throwable, Exception {
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
		conferencing.verifyReplayStatus("Not Requested");
		conferencing.launchLiveDashboard();
		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		participantsDashboardPage.verifyPageTitle("Conference Dashboard");
		participantsDashboardPage.verifyCallName(automationShowName);
		Thread.sleep(8000);
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Available.getCallstatus());
		participantsDashboardPage.verifyRegisteredUser(testDataHelper.getValue("RegisteredUserEmail"));
		participantsDashboardPage.typeMessage("Hello" + automationShowName);
		participantsDashboardPage.clickSubmitMsgBoard();
		participantsDashboardPage.verifyMsgOnChatBoard("Hello" + automationShowName);
		Thread.sleep(8000);
	}
}