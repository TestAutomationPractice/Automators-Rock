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
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class VerifyShowStatus extends Base {

	private static String automationShowName = "AutomationShowStatus" + ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");
	private String entryCode = "EC" + generateNumInStringFormat(10);


	public VerifyShowStatus() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create an Document only Show", enabled = true)
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
		documentaion.verifyShowStatus(ShowStatus.Incomplete.getString());
		documentaion.verifyShowColor(ShowStatusColor.Red.getString());
		documentaion.selectDocumentType("Addendum");
		documentaion.enterDocumentName("Document");
		documentaion.enterDocumentFilename("sample.pdf");
		documentaion.clickUpload();
		documentaion.waitForFileUpload();
		Thread.sleep(5000);
		documentaion.clickSaveAndContinue();

		Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.verifyShowStatus(ShowStatus.Incomplete.getString());
		disclaimer.verifyShowColor(ShowStatusColor.Red.getString());
		disclaimer.verifyPreviewButton();
		disclaimer.clickAndVerifyDocumentPreview();
		disclaimer.clickSaveAndContinue();

		EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.enterEntryCode(entryCode);
		entryCodes.selectEntryCodeType("Review");
		Thread.sleep(3000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.verifyShowStatus(ShowStatus.Incomplete.getString());
		entryCodes.verifyShowColor(ShowStatusColor.Red.getString());
		entryCodes.verifyPreviewButton();
		entryCodes.clickAndVerifyDocumentPreview();
		entryCodes.clickContinue();

		Billing billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.verifyShowStatus(ShowStatus.Incomplete.getString());
		billing.verifyShowColor(ShowStatusColor.Red.getString());
		billing.verifyPreviewButton();
		billing.clickAndVerifyDocumentPreview(); 
		billing.clickSaveAndContinue();
		Thread.sleep(5000);

		Review review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.verifyShowStatus(ShowStatus.Incomplete.getString());
		review.verifyShowColor(ShowStatusColor.Red.getString());
		review.verifyPreviewButton();
		review.clickAndVerifyDocumentPreview();
		review.goToSMHomePage();
		// Navigated back to Home Grid without making a show live

		smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Incomplete.getString());
		smHome.clickShowName(automationShowName);

		showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.verifyShowStatus(ShowStatus.Incomplete.getString());
		showdetails.verifyShowColor(ShowStatusColor.Red.getString());
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.clickSaveAndContinue();
		showdetails.goToSMHomePage();
		smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Red.getString(), ShowStatus.Disabled.getString());
		smHome.clickShowName(automationShowName);

		showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.verifyShowStatus(ShowStatus.Disabled.getString());
		showdetails.verifyShowColor(ShowStatusColor.Red.getString());
		showdetails.clickSaveAndContinue();
		documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.verifyShowStatus(ShowStatus.Disabled.getString());
		documentaion.verifyShowColor(ShowStatusColor.Red.getString());
		documentaion.clickSaveAndContinue();

		disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.verifyShowStatus(ShowStatus.Disabled.getString());
		disclaimer.verifyShowColor(ShowStatusColor.Red.getString());
		disclaimer.clickSaveAndContinue();

		entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.verifyShowStatus(ShowStatus.Disabled.getString());
		entryCodes.verifyShowColor(ShowStatusColor.Red.getString());
		entryCodes.clickContinue();
		billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.verifyShowStatus(ShowStatus.Disabled.getString());
		billing.verifyShowColor(ShowStatusColor.Red.getString());
		billing.clickSaveAndContinue();
		Thread.sleep(5000);

		review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.verifyShowStatus(ShowStatus.Disabled.getString());
		review.verifyShowColor(ShowStatusColor.Red.getString());
		review.clickMakeShowLive();
		review.clickYes();
		Thread.sleep(5000);
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());

		// Navigating back to Home Grid after making a show live		
		review.goToSMHomePage();
		smHome = PageFactory.initElements(driver, SMHome.class);
		smHome.setDependencies(logger, testDataHelper);
		smHome.enterSearchTextSMGrid(automationShowName);
		smHome.verifyShowColorAndStatus(automationShowName, ShowStatusColor.Green.getString(), ShowStatus.Live.getString());

		smHome.clickShowName(automationShowName);

		showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.verifyShowStatus(ShowStatus.Live.getString());
		showdetails.verifyShowColor(ShowStatusColor.Green.getString());
		showdetails.clickSaveAndContinue();

		documentaion = PageFactory.initElements(driver, Documentation.class);
		documentaion.setDependencies(logger, testDataHelper);
		documentaion.verifyShowStatus(ShowStatus.Live.getString());
		documentaion.verifyShowColor(ShowStatusColor.Green.getString());
		documentaion.clickSaveAndContinue();

		disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.verifyShowStatus(ShowStatus.Live.getString());
		disclaimer.verifyShowColor(ShowStatusColor.Green.getString());
		disclaimer.clickSaveAndContinue();

		entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		entryCodes.verifyShowStatus(ShowStatus.Live.getString());
		entryCodes.verifyShowColor(ShowStatusColor.Green.getString());
		entryCodes.clickContinue();
		billing = PageFactory.initElements(driver, Billing.class);
		billing.setDependencies(logger, testDataHelper);
		billing.verifyShowStatus(ShowStatus.Live.getString());
		billing.verifyShowColor(ShowStatusColor.Green.getString());
		billing.clickSaveAndContinue();
		Thread.sleep(5000);

		review = PageFactory.initElements(driver, Review.class);
		review.setDependencies(logger, testDataHelper);
		review.verifyShowStatus(ShowStatus.Live.getString());
		review.verifyShowColor(ShowStatusColor.Green.getString());

	}
}
