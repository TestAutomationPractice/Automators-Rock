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
import Miscellaneous.Enums.CallStatus;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.DeclarationPage;
import Pages.OnlineConfirmationPage;
import Pages.DisclaimerPage;
import Pages.EmailEntryPage;
import Pages.ParticipantsDashboardPage;
import Pages.RegistrationPage;
import Pages.ValidationPage;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.ConferenceCall;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class SmokeTestLiveStreamDisabled extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String connectFNRegistrationLink = "";
	private String companyEmail = "Test" + ComponentBase.Newdatetime() + "@nrs.com";
	private String invalidEmail = "Test@" + generateNumInStringFormat(4) + "@nrs.com";
	private String invalidEmailDomain= "Test" + ComponentBase.Newdatetime() + "@123.com";
	private String invalidPhoneNumber = "12345";
	private Calendar currentDate = Calendar.getInstance();

	public SmokeTestLiveStreamDisabled() {
		testRunType = TestRunType.Smoke;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify Conference Call Creation", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-87,TC-92,TC-96")
	public void verifyMyCallsPage() throws Throwable, Exception {
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
		confCall.enterStartDate(
				ComponentBase.modDate(0, currentDate, new SimpleDateFormat("MM/dd/yyyy"), ComponentBase.TIMEZONE_IST));
		confCall.enterStartTime(
				ComponentBase.modTime(0, currentDate, new SimpleDateFormat("hh:mm a"), ComponentBase.TIMEZONE_IST));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.enablePrivatePublic();
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();
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

	

	@Test(description = "Verify User Registration with one Disclaimer and Private Public Enabled", enabled = true, dependsOnMethods = {"verifyMyCallsPage" })
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyUserRegistrationDisclaimerDeclarationEnabled() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);
		entryPage.verifyCompanyEmail();
		entryPage.enterCompanyEmail(testDataHelper.getValue("RegisteredUserEmail"));
		entryPage.verifyCallTitle(automationShowName);
		/*entryPage.verifyDate(ComponentBase.modDate(0, currentDate, new SimpleDateFormat("MMMM d, yyyy"),
				currentDate.getTimeZone().toString()));
		entryPage.verifyTime(ComponentBase.modTime(0, currentDate, new SimpleDateFormat("h:mm a"),
				currentDate.getTimeZone().toString()));*/
		entryPage.clickContinue();
		Thread.sleep(5000);
		DeclarationPage declarationPage = PageFactory.initElements(driver, DeclarationPage.class);
		declarationPage.setDependencies(logger, testDataHelper);
		declarationPage.selectPrivate();
		declarationPage.clickContinue();
		Thread.sleep(5000);
		DisclaimerPage disclaimerPage = PageFactory.initElements(driver, DisclaimerPage.class);
		disclaimerPage.setDependencies(logger, testDataHelper);
		disclaimerPage.verifyDisclaimerPage();
		disclaimerPage.clickAccept();
		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPage();
		//onlineConfirmationPage.verifyDailInNumbers(testDataHelper.getValue("UnitedStatesTollFree"),testDataHelper.getValue("UnitedStatesLocal"));
		//onlineConfirmationPage.verifyGlobalDailInLink();
		onlineConfirmationPage.verifyConfirmationMail(automationShowName, testDataHelper.getValue("RegisteredUserEmail"));
		Thread.sleep(5000);
	}
	
	@Test(description = "Verify unregistered user to register for a call with  disclaimer and declaration page", enabled = true, dependsOnMethods = {"verifyUserRegistrationDisclaimerDeclarationEnabled" })
	@CustomTestAnnotations(testCaseNumber = "TC-104,TC-108,TC-25,TC-109")
	public void verifyUserRegistration() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);
		entryPage.verifyCompanyEmail();
		entryPage.enterCompanyEmail(companyEmail);
		entryPage.verifyCallTitle(automationShowName);
		/*entryPage.verifyDate(ComponentBase.modDate(0, currentDate, new SimpleDateFormat("MMMM d, yyyy"),
				currentDate.getTimeZone().toString()));
		entryPage.verifyTime(ComponentBase.modTime(0, currentDate, new SimpleDateFormat("h:mm a"),
				currentDate.getTimeZone().toString()));*/
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

		DeclarationPage declarationPage = PageFactory.initElements(driver, DeclarationPage.class);
		declarationPage.setDependencies(logger, testDataHelper);
		declarationPage.selectPublic();
		declarationPage.clickContinue();
		Thread.sleep(5000);
		DisclaimerPage disclaimerPage = PageFactory.initElements(driver, DisclaimerPage.class);
		disclaimerPage.setDependencies(logger, testDataHelper);
		disclaimerPage.verifyDisclaimerPage();
		disclaimerPage.clickAccept();
		OnlineConfirmationPage onlineConfirmationPage = PageFactory.initElements(driver, OnlineConfirmationPage.class);
		onlineConfirmationPage.setDependencies(logger, testDataHelper);
		onlineConfirmationPage.verifyDialInPage();
		//onlineConfirmationPage.verifyGlobalDailInLink();
		onlineConfirmationPage.verifyConfirmationMail(automationShowName, companyEmail);
		Thread.sleep(5000);
	}

	@Test(description = "Verify Conference Call Live Dashboard that registered User is added", enabled = true, dependsOnMethods = {"verifyUserRegistration" })
	@CustomTestAnnotations(testCaseNumber = "TC-100,TC-106,TC-28,TC-32")
	public void verifyLiveDashboardRegisteredUser() throws Throwable, Exception {
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

		ParticipantsDashboardPage participantsDashboardPage = PageFactory.initElements(driver,
				ParticipantsDashboardPage.class);
		participantsDashboardPage.setDependencies(logger, testDataHelper);
		participantsDashboardPage.verifyPageTitle("Conference Dashboard");
		participantsDashboardPage.verifyCallName(automationShowName);
		Thread.sleep(8000);
		participantsDashboardPage.printCallStatus();
		participantsDashboardPage.verifyCallStatus(CallStatus.Available.getCallstatus());
		participantsDashboardPage.verifyRegisteredUser(testDataHelper.getValue("RegisteredUserEmail"));
		participantsDashboardPage.revokeRegisteredUserAccess();
		participantsDashboardPage.restoreRegisteredUserAccess();
		participantsDashboardPage.verifyRevokedUserMail(automationShowName,
				testDataHelper.getValue("RegisteredUserEmail"));
		participantsDashboardPage.verifyRestoredUserMail(automationShowName,
				testDataHelper.getValue("RegisteredUserEmail"));
		
		participantsDashboardPage.verifyRegisteredUser(companyEmail);
		participantsDashboardPage.clickLogout();
		participantsDashboardPage.typeMessage("Hello" + automationShowName);
		participantsDashboardPage.clickSubmitMsgBoard();
		Thread.sleep(2000);
		participantsDashboardPage.verifyMsgOnChatBoard("Hello" + automationShowName);
	}

	
	/*
	
	
 @Test(description = "Verify error messages on email entry form and registration form", enabled = true, dependsOnMethods = {"verifyMyCallsPage" })
 @CustomTestAnnotations(testCaseNumber = "TC-110, TC-114, TC-115, TC-125, TC-131")
 public void verifyerrormessagesonemailentryformandregistrationform() throws Throwable, Exception {
       CommonTestDataDto testDataDto = commonTestData.get();
       RemoteWebDriver driver = testDataDto.getDriver();
       Log4JLogger logger = testDataDto.getLogger();
       TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
       driver.get(connectFNRegistrationLink);
       Thread.sleep(5000);
       EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
       entryPage.setDependencies(logger, testDataHelper);
       entryPage.verifyCompanyEmail();
       entryPage.clickContinue();
       entryPage.verifyCompanyEmailValidation();     
       entryPage.enterCompanyEmail(invalidEmail);
       entryPage.clickContinue();
       entryPage.verifycompanyinvalidemailvalidation();
       entryPage.clearemail();
       entryPage.enterCompanyEmail(invalidEmailDomain);
       entryPage.clickContinue();
       entryPage.verifyCompanyInvalidEmailDomain();
       entryPage.clearemail();         
       String newCompanyEmail = "RT" + ComponentBase.Newdatetime() + "@nrs.com";
       entryPage.enterCompanyEmail(newCompanyEmail);
       entryPage.clickContinue();
       
       RegistrationPage registration = PageFactory.initElements(driver, RegistrationPage.class);
		registration.setDependencies(logger, testDataHelper);
		Thread.sleep(4000);
		registration.registrationLogin();
		registration.verifyFirstNameValidation();
		registration.verifyLastNameValidation();
		registration.verifyYourCompanyValidation();
		registration.verifyYourCountryValidation();
		registration.verifyYourCompanyPhone();
		
		registration.enterfirstname(testDataHelper.getValue("FirstName"));
		registration.enterlastname(testDataHelper.getValue("LastName"));
		registration.entercompany(testDataHelper.getValue("Company"));
		registration.entercountry(testDataHelper.getValue("Country"));
		
		registration.entercompanyphone(invalidPhoneNumber);
		registration.registrationLogin();
		registration.verifyInvalidCompanyPhone();
		registration.clearPhoneNumber();		
		registration.entercompanyphone(testDataHelper.getValue("CompanyPhone"));
		Thread.sleep(2000);
		registration.verifyemailnoteditable();
		registration.registrationLogin();
        Thread.sleep(5000);
        
        ValidationPage validation = PageFactory.initElements(driver, ValidationPage.class);
		validation.setDependencies(logger, testDataHelper);
		validation.verifyEmailRegistrationText();
		validation.verifyYourEmail(newCompanyEmail);
		validation.verifyTechSupport();
	}*/
 
	/*@Test(description = "Verify the footer information", enabled = true, dependsOnMethods = { "verifyMyCallsPage" })
	@CustomTestAnnotations(testCaseNumber = "TC-113")
	public void verifythefooterinformation() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		driver.get(connectFNRegistrationLink);
		Thread.sleep(5000);
		EmailEntryPage entryPage = PageFactory.initElements(driver, EmailEntryPage.class);
		entryPage.setDependencies(logger, testDataHelper);
		entryPage.verifyCompanyEmail();
		entryPage.verifytechsupportlink();
		entryPage.verifyclickingsupportlink();
		entryPage.verifyconnectfnlogo();
		entryPage.verifyprivacypolicylink();
		entryPage.verifyclickingprivacypolicylink();

	}*/
}

