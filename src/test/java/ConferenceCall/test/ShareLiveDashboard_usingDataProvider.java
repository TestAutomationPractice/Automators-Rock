package ConferenceCall.test;

import java.util.HashMap;
import java.util.Set;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Conferencing.Test.*;
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

public class ShareLiveDashboard_usingDataProvider extends Base {

	private static String automationShowName = "AutoConfCall1" + ComponentBase.dateTime();
	private static String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String message = "Hello, hope everthing is fine !!! ";
	private HashMap<String, String> urlListFromMails = new HashMap<>();
	private String randomUser = "autoru" + generateNumInStringFormat(10) + "@test.com";

	public ShareLiveDashboard_usingDataProvider() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify Conference Call Creation", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-87,TC-92,TC-96")
	public void createCFNCall() throws Throwable, Exception {
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
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(0));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
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
	}

	@Test(description = "Verify Sharing Live Dashboard with guest User", enabled = true, dependsOnMethods = {
			"createCFNCall" })
	@CustomTestAnnotations(testCaseNumber = "TC-100")
	public void verifySharingConferenceDashboard() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		
		String[] users = testDataHelper.getValue("SharedUserList").split(",");
		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMURL"), userName, password, testDataDto);
		NRSHome nrsHome = PageFactory.initElements(driver, NRSHome.class);
		nrsHome.setDependencies(logger, testDataHelper);
		nrsHome.enterEntryCode(entryCode);
		nrsHome.clickAddEntryCode();
		for (int i = 0; i < users.length; i++) {
			Thread.sleep(3000);
			nrsHome.clickShowName(automationShowName);
			Thread.sleep(3000);
			nrsHome.clickShareDashboard();
			Thread.sleep(3000);
			nrsHome.enterEmail(users[i]);
			nrsHome.clickShare();
			String url = ComponentBase.getLiveDashboardLinkMailtrap(automationShowName, users[i]);
			urlListFromMails.put(users[i], url);
		}
		Thread.sleep(3000);
		nrsHome.clickShowName(automationShowName);
		nrsHome.clickShareDashboard();
		Thread.sleep(3000);
		nrsHome.enterEmail(randomUser);
		nrsHome.clickShare();
		String url = ComponentBase.getLiveDashboardLinkMailtrap(automationShowName, randomUser);
		urlListFromMails.put(randomUser, url);
	}

	@Test(dataProvider = "UserList", description = "Verify Shared Live Dashboard with guest User", enabled = true, dependsOnMethods = {
			"verifySharingConferenceDashboard" })
	@CustomTestAnnotations(testCaseNumber = "TC-87")
	public void testMethod(String loggedInUser, String sharedUser, boolean flag) throws Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();

		logger.log("########### Logged in User: " + loggedInUser + " , Shared User: " + sharedUser + " #########");
			
		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMURL"), loggedInUser,
				testDataHelper.getValue("LoggedInUserPassword"), testDataDto);
		Thread.sleep(5000);

		ConferenceDashboardGuestLoginPage guestLogin = PageFactory.initElements(driver,
				ConferenceDashboardGuestLoginPage.class);
		guestLogin.setDependencies(logger, testDataHelper);
		guestLogin.launchGuestLogin(urlListFromMails.get(sharedUser));
		
		if ((!loggedInUser.equals(sharedUser) && flag) || (loggedInUser.equals(sharedUser) && flag)) {
			guestLogin.enterFirstName(automationShowName.substring(0, 10));
			guestLogin.enterLastName(automationShowName.substring(0, 10));
			guestLogin.enterEmail(sharedUser);
			guestLogin.enterCompany(automationShowName.substring(0, 10));
			guestLogin.clickViewDashboard();
		}

		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		Thread.sleep(5000);
		participantsDashboardPage.verifyPageTitle("Live Dashboard");
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Available.getCallstatus());
		participantsDashboardPage.typeMessage(message + sharedUser);
		participantsDashboardPage.clickSubmitMsgBoard();
		participantsDashboardPage.verifyUserMsgOnChatBoard(message + sharedUser);
	}

	@Test(dataProvider = "UserList2" , description = "Verify Sharing Live Dashboard with guest User", enabled = true, dependsOnMethods = {"verifySharingConferenceDashboard" })
	@CustomTestAnnotations(testCaseNumber = "TC-100")
	public void verifyLiveDashboard( String sharedUser) throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();

		ConferenceDashboardGuestLoginPage guestLogin = PageFactory.initElements(driver, ConferenceDashboardGuestLoginPage.class);
		guestLogin.setDependencies(logger, testDataHelper);
		logger.log("########### Shared User: " + sharedUser + " #########");
		guestLogin.launchGuestLogin(urlListFromMails.get(sharedUser));
		Thread.sleep(5000);
		guestLogin.enterFirstName(automationShowName.substring(0,10));
		guestLogin.enterLastName(automationShowName.substring(0,10));
		guestLogin.enterEmail(sharedUser);
		guestLogin.enterCompany(automationShowName.substring(0,10));
		guestLogin.clickViewDashboard();
		
		
		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		Thread.sleep(5000);
		participantsDashboardPage.verifyPageTitle("Live Dashboard");
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Available.getCallstatus());
		participantsDashboardPage.typeMessage(message);
		participantsDashboardPage.clickSubmitMsgBoard();
		Thread.sleep(5000);
	}
	
	@DataProvider(name = "UserList")
	public Object[][] getDataFromDataprovider() {
		/*Set<String> userEmails = urlListFromMails.keySet();
		String[] userEmail = new String[userEmails.size()];
		int index = 0;
		for (String element : userEmails)
			userEmail[index++] = element; */

		CommonTestDataDto testDataDto = commonTestData.get();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		String[] userEmail = testDataHelper.getValue("SharedUserList").split(",");
		
		return new Object[][] { 
			{ userEmail[0], userEmail[0] , false}, 	
			{ "AutoRegularUser@automation.com", userEmail[0] , true},  
			{ userEmail[1], userEmail[1] , false},   	//primary primary
			{ userEmail[1], userEmail[2] , true}, 	//primary secondary
			{ userEmail[2], userEmail[2] , true},		//secondary secondary
			{ userEmail[2], userEmail[1] , false}, 	//secondary primary
			{ userEmail[3], userEmail[3] , false} };	//producer producer
	}
	
	@DataProvider(name = "UserList2")
	public Object[][] getDataFromDataprovider2() {
		Set<String> userEmails = urlListFromMails.keySet();
		String[] userEmail = new String[userEmails.size()];
		int index = 0;
		for (String element : userEmails)
			userEmail[index++] = element; 
		return new Object[][]{ 
			{userEmail[0]} , 	
			{userEmail[1]} ,  
			{userEmail[2] },   	
			{userEmail[3] }, 
			{ randomUser } };	
	}
}
