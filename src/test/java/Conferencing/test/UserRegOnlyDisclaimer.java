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
import Pages.DisclaimerPage;
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
import junit.framework.Assert;

public class UserRegOnlyDisclaimer extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private String companyEmail = "Test" + ComponentBase.Newdatetime() + "@nrs.com";
	
	public UserRegOnlyDisclaimer() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify ConnectFN Conference Call Creation with Disclaimer", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-112")
	public void creatConnectFNCallWithDisclaimer() throws Throwable, Exception {
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
		confCall.selectReplayMode("None");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(10));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
		
		Thread.sleep(5000);
		confCall.verifyEventIdGrid();
		confCall.verifyCountOfDigitsInGrid();
		confCall.conferenceGridEdit();
		Thread.sleep(3000);
		confCall.verifyEventIdForm();
		confCall.verifyCountOfDigitsInForm();
		
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

	//@Test(description = "Verify User Registration with one Disclaimer", enabled = true, dependsOnMethods = {"creatConnectFNCallWithDisclaimer" })
	@CustomTestAnnotations(testCaseNumber = "TC-112")
	public void verifyUserRegistrationDeclarationEnabled() throws Throwable, Exception {
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
		OnlineConfirmationPage dailInDetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		dailInDetails.setDependencies(logger, testDataHelper);
		dailInDetails.verifyDialInPage();
		Thread.sleep(5000);
		
	}
	
	
	//@Test(description = "Verify unregistered user to register for a call with a disclaimer and no declaration page", enabled = true, dependsOnMethods= {"creatConnectFNCallWithDisclaimer"})
	@CustomTestAnnotations(testCaseNumber = "TC-116")
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
		
		DisclaimerPage disclaimerPage = PageFactory.initElements(driver, DisclaimerPage.class);
		disclaimerPage.setDependencies(logger, testDataHelper);
		disclaimerPage.verifyDisclaimerPage();
		disclaimerPage.clickAccept();
		
		OnlineConfirmationPage dialindetails = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		dialindetails.setDependencies(logger, testDataHelper);
		dialindetails.verifyDialInPage();
		Thread.sleep(5000);
	}
	
	//@Test(description = "Verify type ahead control function on the company field", enabled = true, dependsOnMethods= {"creatConnectFNCallWithDisclaimer"})
	@CustomTestAnnotations(testCaseNumber = "TC-128")
	public void verifyTypeAheadControlFunctionOnCompanyField() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		navigateToURL(testDataDto, connectFNRegistrationLink);
		Thread.sleep(4000);
		
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);
		String newCompanyEmail= "Test" + ComponentBase.Newdatetime() + "@jpmorgan.com";
		entryPage.enterCompanyEmail(newCompanyEmail);
		entryPage.clickContinue();
		
		RegistrationPage registration = PageFactory.initElements(driver, RegistrationPage.class);
		registration.setDependencies(logger, testDataHelper);
		registration.enterfirstname(testDataHelper.getValue("FirstName"));
		registration.enterlastname(testDataHelper.getValue("LastName"));
		registration.entercountry(testDataHelper.getValue("Country"));
		registration.entercompanyphone(testDataHelper.getValue("CompanyPhone"));
		registration.entercompany("J");
		Thread.sleep(3000);
		int sizeElement = registration.verifyListOfElements();		
		registration.entercompany("P");
		int sizeElement1 = registration.verifyListOfElements();
		if(sizeElement1>sizeElement)
		Assert.fail("The size of new list is greater than previous list");
		Thread.sleep(3000);		
		String newCompanyName= " Morgan US";
		registration.entercompany(newCompanyName);		
		registration.registrationLogin();
		
		ValidationPage validation = PageFactory.initElements(driver, ValidationPage.class);
		validation.setDependencies(logger, testDataHelper);
		validation.verifyCheckEmailText();
		validation.verifyYourEmail(newCompanyEmail);
		
	}
}
