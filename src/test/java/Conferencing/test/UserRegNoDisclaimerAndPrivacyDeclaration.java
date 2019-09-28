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

public class UserRegNoDisclaimerAndPrivacyDeclaration extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private String companyEmail = "Test" + ComponentBase.Newdatetime() + "@nrs.com";	
	public  String ccTopic="Rtest "+ generateNumInStringFormat(25)+" "+"Rtest "+ generateNumInStringFormat(25)+" "+ generateNumInStringFormat(150)+" "+"Rtest "+ generateNumInStringFormat(150)+" "+"Rtest "+ generateNumInStringFormat(150)+" "+"Rtest "+ generateNumInStringFormat(150)+" "+"Rtest "+ generateNumInStringFormat(150);
	
	public UserRegNoDisclaimerAndPrivacyDeclaration() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify ConnectFN Conference Call Creation No Disclaimer and No Privacy Declaration", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-111")
	public void creatConnectFNCall() throws Throwable, Exception {
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
		confCall.enterTopic(ccTopic);
		confCall.enableRegistration();
		confCall.selectReplayMode("None");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(10));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		connectFNRegistrationLink = confCall.clickAndCopyRegistrationLink();
		logger.log("ConnectFN Link : " + connectFNRegistrationLink);
		confCall.clickOkayButton();
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

	@Test(description = "Verify User Registration with one Disclaimer and Private Public and Sales Rep Enabled", enabled = true, dependsOnMethods = {"creatConnectFNCall" })
	@CustomTestAnnotations(testCaseNumber = "TC-111")
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
		OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		dailInDetails.setDependencies(logger, testDataHelper);
		dailInDetails.verifyDialInPage();
		Thread.sleep(5000);
	}
	
	@Test(description = "Verify unregistered user to register for a call with no disclaimer and no declaration page", enabled = true, priority = 2, dependsOnMethods= {"creatConnectFNCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-111")
	public void verifyLiveDashboardPage() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, connectFNRegistrationLink);
		Thread.sleep(4000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);		
		entryPage.verifyCompanyEmail();						
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
		OnlineConfirmationPage dialindetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		dialindetails.setDependencies(logger, testDataHelper);
		dialindetails.verifyDialInPage();
	}
	
	@Test(description = "Verify user has not validated email and attempts to re-register for the call, a new validation link will be sent", enabled = true, priority = 2, dependsOnMethods= {"creatConnectFNCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-111, TC-117, TC-118")
	public void verifyNewValidationLink() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, connectFNRegistrationLink);
		Thread.sleep(4000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);		
		entryPage.verifyCompanyEmail();
		String newCompanyEmail = "NT" + ComponentBase.Newdatetime() + "@nrs.com";
	    entryPage.enterCompanyEmail(newCompanyEmail);
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
		validation.verifyYourEmail(newCompanyEmail);
		validation.verifyWhetherEmailExistsInMailTrap(automationShowName, newCompanyEmail);
		
		navigateToURL(testDataDto, connectFNRegistrationLink);
		entryPage.enterCompanyEmail(newCompanyEmail);
		entryPage.clickContinue();
		validation.verifyWhetherEmailExistsInMailTrap(automationShowName, newCompanyEmail);
		validation.verifyValidationLinkRequestPage();
	}
	
	@Test(description = "Verify user is able to validate topic & manager name in the investor screen", enabled = true, priority = 2, dependsOnMethods= {"creatConnectFNCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-120, TC-126")
	public void verifyTopicManagerName() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, connectFNRegistrationLink);
		Thread.sleep(4000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);		
		entryPage.verifyCompanyEmail();	
	    entryPage.verifyTopic(ccTopic.substring(0, 100));	    
	    entryPage.verifyManager(testDataHelper.getValue("Underwriter"));
	    entryPage.verifySeeMore();
	    entryPage.clickSeeMore();
	    entryPage.clickCloseIcon();
	}	
}