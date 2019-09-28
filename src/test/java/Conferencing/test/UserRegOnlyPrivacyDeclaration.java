package Conferencing.test;

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
import Pages.DeclarationPage;
import Pages.EmailEntryPage;
import Pages.RegistrationPage;
import Pages.ValidationPage;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.ConferenceCall;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class UserRegOnlyPrivacyDeclaration extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private String companyEmail = "Test" + ComponentBase.Newdatetime() + "@nrs.com";
	String conferenceEventId;

	public UserRegOnlyPrivacyDeclaration() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify ConnectFN Conference Call Creation with Enable Public Private and Sales Rep", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void creatConnectFNCallWithPrivacyDeclaration() throws Throwable, Exception {
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
		confCall.enterStartTime(ComponentBase.modTime(10));
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
		Thread.sleep(5000);
		confCall.clickOkayButton();
		confCall.verifyEventIdGrid();
		conferenceEventId=confCall.eventIdValue();
		System.out.println("Value of conference id:" +conferenceEventId);
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

	@Test(description = "Verify User Registration with one Disclaimer and Private Public and Sales Rep Enabled", enabled = true, dependsOnMethods = {"creatConnectFNCallWithPrivacyDeclaration" })
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyUserRegistrationPrivacyDeclarationEnabled() throws Throwable, Exception {
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
		declarationPage.selectPrivate();
		declarationPage.enterSalesRepName("TestSales");
		declarationPage.clickContinue();
		OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		dailInDetails.setDependencies(logger, testDataHelper);
		dailInDetails.verifyDialInPage();
		Thread.sleep(5000);
	}
	
	@Test(description = "Verify unregistered user to register for a call with no disclaimer but has a declaration page", enabled = true, dependsOnMethods= {"creatConnectFNCallWithPrivacyDeclaration"})
	@CustomTestAnnotations(testCaseNumber = "TC-104,TC-123")
	public void verifyDisclaimerNoDeclarationPage() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, connectFNRegistrationLink);
		Thread.sleep(4000);
		
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);						
		entryPage.enterCompanyEmail(companyEmail);
		entryPage.clickContinue();
		
		RegistrationPage registration = PageFactory.initElements(driver, RegistrationPage.class);
		registration.setDependencies(logger, testDataHelper);
		registration.enterfirstname(testDataHelper.getValue("FirstName"));
		registration.enterlastname(testDataHelper.getValue("LastName"));
		registration.entercompany(testDataHelper.getValue("Company"));
		registration.entercountry(testDataHelper.getValue("Country"));
		registration.entercompanyphone(testDataHelper.getValue("CompanyPhone"));
		registration.registrationLogin();
		
		ValidationPage validation = PageFactory.initElements(driver, ValidationPage.class);
		validation.setDependencies(logger, testDataHelper);
		validation.verifyCheckEmailText();
		validation.verifyYourEmail(companyEmail);
		validation.verifyMyEmail(automationShowName, companyEmail);
		Thread.sleep(3000);
		
		DeclarationPage declarationPage = PageFactory.initElements(driver, DeclarationPage.class);
		declarationPage.setDependencies(logger, testDataHelper);
		declarationPage.selectPrivate();
		declarationPage.enterSalesRepName("TestSales");
		declarationPage.clickContinue();
		
		OnlineConfirmationPage onlineconfirmationpage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineconfirmationpage.setDependencies(logger, testDataHelper);
		onlineconfirmationpage.verifyDialInPage();
		Thread.sleep(4000);
		onlineconfirmationpage.verifyAutomatedTollFreeText();		
		onlineconfirmationpage.verifyAutomatedLocalText(); 		
		onlineconfirmationpage.verifyDailInNumbers(testDataHelper.getValue("UnitedStatesTollFree"), testDataHelper.getValue("UnitedStatesLocal"));
		onlineconfirmationpage.verifyYourPin();
		onlineconfirmationpage.verifyEventIdText();
		onlineconfirmationpage.verifyEventId(conferenceEventId);
		onlineconfirmationpage.verifyPersonalAssistanceText();
				
	}
	
}