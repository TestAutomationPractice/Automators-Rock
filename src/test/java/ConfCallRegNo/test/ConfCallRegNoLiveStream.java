package ConfCallRegNo.test;

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
import Pages.MYNRS.GuestLogin;
import Pages.MYNRS.PresentaionUI;
import Pages.MYNRS.SplashPage;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.ConferenceCall;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class ConfCallRegNoLiveStream extends Base {

	private String automationShowName = "AutoConfCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String confCallName = "ConfCall" + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(10);
	private String directLink = "";
	
	public ConfCallRegNoLiveStream() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Verify Conference Call Registration No Creation", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void creatConfCall() throws Throwable, Exception {
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
		confCall.disableRegistration();
		confCall.enableLiveStreaming();
		confCall.selectReplayMode("None");
		confCall.enterStartDate(ComponentBase.modDate(0));
		confCall.enterStartTime(ComponentBase.modTime(15));
		confCall.enterEndDate(ComponentBase.modDate(0));
		confCall.enterEndTime(ComponentBase.modTime(50));
		confCall.clickAndAddEntryCode(entryCode);
		confCall.selectClientProfile(testDataHelper.getValue("ClientPofile"));
		Thread.sleep(5000);
		confCall.clickAdd();		
		Thread.sleep(5000);
		directLink = confCall.clickAndCopyDirectShowLink();
		logger.log("Direct Show Link : " + directLink);
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

	@Test(description = "Verify Adding entry code and Presentation launch", enabled = true, dependsOnMethods = {"creatConfCall"})
	@CustomTestAnnotations(testCaseNumber = "TC-104")
	public void verifyUserRegistrationDisclaimerDeclarationEnabled() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();

		GuestLogin guestLogin = PageFactory.initElements(driver, GuestLogin.class);
		guestLogin.setDependencies(logger, testDataHelper);
		guestLogin.launchShow(directLink, testDataHelper.getValue("RegisteredUserEmail"), testDataDto);
		Thread.sleep(5000);	
		SplashPage splashPage = PageFactory.initElements(driver, SplashPage.class);
		splashPage.setDependencies(logger, testDataHelper);
		splashPage.verifySlideShowCard();
		splashPage.verifyStreamingIsAvailableBefore10mins();
		splashPage.verifyDialInCardExists();
		splashPage.clickDialInCard();
		splashPage.verifyAutomaticConnectLabel();
		Thread.sleep(2000);
		splashPage.verifyNoAudioCardExists();
		splashPage.clickNoAudioCard();
		Thread.sleep(2000);
		splashPage.verifyLaunchPresentationButton();
		splashPage.verifyNoAudioLabel();
		splashPage.verifyStreamingCardExists();
		splashPage.clickStreamingCard();
		Thread.sleep(2000);
		splashPage.clickTestAudio();
		Thread.sleep(1000);
		splashPage.verifyPlayingAudioSample();
		splashPage.verifyLaunchPresentationButton();
		splashPage.clickLaunchPresentationButton();
		Thread.sleep(5000);		
		PresentaionUI presentationUI = PageFactory.initElements(driver, PresentaionUI.class);
		presentationUI.setDependencies(logger, testDataHelper);
		/*presentationUI.verifyStreamingTab();
		presentationUI.verifyDialInTab();
		presentationUI.verifyStreamPlayer();*/
		presentationUI.verifyStreamingWorksForOneMinute();
				
		Thread.sleep(8000);
	}

}