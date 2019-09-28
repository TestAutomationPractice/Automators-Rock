package SM.test;

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
import Miscellaneous.Enums.ShowStatus;
import Miscellaneous.Enums.ShowStatusColor;
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.Duplicate;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;


public class DuplicateDocOnlyShow extends Base{
	private static String automationShowName = "AutoSMDuplicateShow" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");
	private String entryCode = "EC" + generateNumInStringFormat(10);


	public DuplicateDocOnlyShow() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}
	@Test(description = "Create an incomplete show and duplicate Show", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createshow() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS

		navigateToShowManagement(testDataDto, userName, password);
		SMHome smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.clickCreateNew();
		System.out.println(automationShowName);
		ShowDetails showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.enterUnderwriter(testDataHelper.getValue("Underwriter"));
		showdetails.enterunderwriterRole(testDataHelper.getValue("UnderwriterRole"));
		showdetails.clickAdd();
		showdetails.selectMediaType(ShowType.Document_Only.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();

		Documentation documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Document");
		documentaion.enterDocumentFilename("sample.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();
		Thread.sleep(5000);
		documentaion.clickSaveAndContinue();

		Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.clickSaveAndContinue();

		EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.enterEntryCode(entryCode);
		entryCodes.selectEntryCodeType("Review");
		Thread.sleep(3000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.clickContinue();

		Billing billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.clickSaveAndContinue();
		Thread.sleep(5000);

		Review review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Incomplete.getString());
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		Duplicate duplicate = PageFactory.initElements(driver, Duplicate.class);
		duplicate.setDependencies(logger, testDataHelper);


		String DuplicateShowName = "AutoSMDuplicateIncomp" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");



		duplicate.enterShowName(DuplicateShowName);
		duplicate.enterShowTitle(DuplicateShowName);
		duplicate.enterStartDate(ComponentBase.modDate(-3));
		duplicate.enterEndDate(ComponentBase.modDate(5));
		duplicate.clickDuplicatebutton();
		duplicate.goToSMHomePage();

		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);	

		showdetails.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Incomplete.getString());
		smHome.clickShowName(automationShowName);

		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.clickSaveAndContinue();

		documentaion.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Disabled.getString());
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutoSMDuplicateDisabled" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");
		duplicate.enterShowName(DuplicateShowName);
		duplicate.enterShowTitle(DuplicateShowName);
		duplicate.enterStartDate(ComponentBase.modDate(-3));
		duplicate.enterEndDate(ComponentBase.modDate(5));
		duplicate.clickDuplicatebutton();


		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);	


		showdetails.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Disabled.getString());
		smHome.clickShowName(automationShowName);

		showdetails.clickSaveAndContinue();

		documentaion.clickSaveAndContinue();

		disclaimer.clickSaveAndContinue();

		entryCodes.clickContinue();

		billing.clickSaveAndContinue();
		Thread.sleep(5000);
		review.clickMakeShowLive();
		review.clickYes();
		Thread.sleep(5000);
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());

		review.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Green.getString(), ShowStatus.Live.getString());
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutoSMDuplicateLiveShow" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		duplicate.enterShowName(DuplicateShowName);
		duplicate.enterShowTitle(DuplicateShowName);
		duplicate.enterStartDate(ComponentBase.modDate(-3));
		duplicate.enterEndDate(ComponentBase.modDate(5));
		duplicate.clickShowTitle();
		duplicate.clickDocumentDuplicatecheckbox();
		duplicate.clickManagerDuplicatecheckbox();
		duplicate.clickDuplicatebutton();
		
		
		showdetails.isManagerPresent(true);
		showdetails.clickSaveAndContinue();
		documentaion.VerifyNumberofDocuments("Addendum",1);
		documentaion.clickSaveAndContinue();
		disclaimer.clickSaveAndContinue();
		entryCodes.clickContinue();
		billing.clickSaveAndContinue();
		Thread.sleep(5000);
		review.clickMakeShowLive();
		review.clickYes();
		Thread.sleep(5000);
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());
		review.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.verifyShowColorAndStatus(DuplicateShowName, ShowStatusColor.Green.getString(), ShowStatus.Live.getString());

		/*

		showdetails.goToSMHomePage();
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickShowName(automationShowName);

		showdetails.selectMediaType(ShowType.Slides_Only.getShowType());
		showdetails.clickSaveAndContinue();
		Slides slides = PageFactory.initElements(driver, Slides.class);
		slides.setDependencies(logger, testDataHelper);
		slides.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutomationDuplicateSlideShow" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		smHome.enterShowName(DuplicateShowName);
		smHome.enterShowTitle(DuplicateShowName);
		smHome.enterStartDate(ComponentBase.modDate(-3));
		smHome.enterEndDate(ComponentBase.modDate(5));
		smHome.clickDuplicatebutton();
		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);

		showdetails.goToSMHomePage();


		// Change to Audio Slide and duplicate
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickShowName(automationShowName);

		showdetails.selectMediaType(ShowType.Audio_Slides.getShowType());
		showdetails.clickSaveAndContinue();

		slides.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutomationDuplicateAudioSlideShow" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		smHome.enterShowName(DuplicateShowName);
		smHome.enterShowTitle(DuplicateShowName);
		smHome.enterStartDate(ComponentBase.modDate(-3));
		smHome.enterEndDate(ComponentBase.modDate(5));
		smHome.clickDuplicatebutton();
		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);

		showdetails.goToSMHomePage();	


		// Change to Video Slide and duplicate
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickShowName(automationShowName);

		showdetails.selectMediaType(ShowType.Video_Slides.getShowType());
		showdetails.clickSaveAndContinue();

		slides.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutomationDuplicateVidioSlide" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		smHome.enterShowName(DuplicateShowName);
		smHome.enterShowTitle(DuplicateShowName);
		smHome.enterStartDate(ComponentBase.modDate(-3));
		smHome.enterEndDate(ComponentBase.modDate(5));
		smHome.clickDuplicatebutton();
		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);

		showdetails.goToSMHomePage();

		// Change to Conference Slide and duplicate
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickShowName(automationShowName);

		showdetails.selectMediaType(ShowType.ConferenceCall_Slides.getShowType());
		showdetails.clickSaveAndContinue();

		slides.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutomationDuplicateConferenceSlide" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		smHome.enterShowName(DuplicateShowName);
		smHome.enterShowTitle(DuplicateShowName);
		smHome.enterStartDate(ComponentBase.modDate(-3));
		smHome.enterEndDate(ComponentBase.modDate(5));
		smHome.clickDuplicatebutton();
		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);

		showdetails.goToSMHomePage();	

		// Change to Conference Only and duplicate
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickShowName(automationShowName);

		showdetails.selectMediaType(ShowType.ConferenceCall.getShowType());
		showdetails.clickSaveAndContinue();

		documentaion.goToSMHomePage();

		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.clickMoreOptions();
		smHome.clickDuplicateOption();
		DuplicateShowName = "AutomationDuplicateConferenceOnly" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");

		smHome.enterShowName(DuplicateShowName);
		smHome.enterShowTitle(DuplicateShowName);
		smHome.enterStartDate(ComponentBase.modDate(-3));
		smHome.enterEndDate(ComponentBase.modDate(5));
		smHome.clickDuplicatebutton();
		smHome.goToSMHomePage();
		smHome.enterSearchTextSMGrid(DuplicateShowName);
		smHome.clickShowName(DuplicateShowName);

		showdetails.goToSMHomePage();	

		 */
	}
}
