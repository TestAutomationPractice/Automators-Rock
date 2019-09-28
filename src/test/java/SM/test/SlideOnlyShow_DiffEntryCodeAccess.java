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
import Miscellaneous.Enums.ShowType;
import Miscellaneous.Enums.TestRunType;
import Miscellaneous.Enums.UserRole;
import Pages.ShowManagement.Billing;
import Pages.ShowManagement.Disclaimer;
import Pages.ShowManagement.Documentation;
import Pages.ShowManagement.EntryCodes;
import Pages.ShowManagement.NRSHome;
import Pages.ShowManagement.NRSLogin;
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;
import Pages.ShowManagement.Slides;

public class SlideOnlyShow_DiffEntryCodeAccess extends Base {

	private static String automationShowName = "AutomationShow"
			+ ComponentBase.dateTime().replaceAll("/", "").replaceAll(":", "");
	private String entryCode1 = "EC1" + generateNumInStringFormat(10);
	private String entryCode2 = "EC2" + generateNumInStringFormat(10);
	private String userName2 = Configuration.getInstance().getUwAdminUserName();
	private String password2 = Configuration.getInstance().getUwAdminPassword();

	public SlideOnlyShow_DiffEntryCodeAccess() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}

	@Test(description = "Create a Slide only Show", enabled = true)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createshow() throws Throwable, Exception {
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
		showdetails.selectMediaType(ShowType.Slides_Only.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(5));
		showdetails.selectTimezone(testDataHelper.getValue("Timezone"));
		showdetails.clickSaveAndContinue();

		Slides slides = PageFactory.initElements(driver, Slides.class);
		slides.setDependencies(logger, testDataHelper);
		slides.enterSlideFilename("sample.pdf");
		slides.clickUpload();
		slides.waitForFileUpload();
		Thread.sleep(5000);
		slides.clickSaveAndContinue();

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
		entryCodes.enterEntryCode(entryCode1);
		entryCodes.selectAccess("Full Access");
		Thread.sleep(3000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.enterEntryCode(entryCode2);
		entryCodes.selectAccess("Doc Only");
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
		review.clickMakeShowLive();
		review.clickYes();
		Thread.sleep(5000);
	}

	@Test(description = "Verify adding Full Access Entry Code", enabled = true, dependsOnMethods = { "createshow" })
	@CustomTestAnnotations(testCaseNumber = "TC 100")
	public void verifyAddingEntryCode() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();

		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMURL"), userName, password, testDataDto);
		NRSHome nrsHome = PageFactory.initElements(driver, NRSHome.class);
		nrsHome.setDependencies(logger, testDataHelper);
		nrsHome.enterEntryCode(entryCode1);
		nrsHome.clickAddEntryCode();
		Thread.sleep(3000);
		nrsHome.clickShowName(automationShowName);
		nrsHome.verifyDocumentNameRightPanel("Document");
		nrsHome.clickViewPresentationAndGetDocumentCount(automationShowName);
	}

	@Test(description = "Verify adding Doc only Entry Code", enabled = true, dependsOnMethods = { "createshow" })
	@CustomTestAnnotations(testCaseNumber = "TC-100")
	public void verifyAddingEntryCodeDocOnly() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();

		NRSLogin nrsLogin = PageFactory.initElements(driver, NRSLogin.class);
		nrsLogin.setDependencies(logger, testDataHelper);
		nrsLogin.loginSM(testDataHelper.getValue("SMURL"), userName2, password2, testDataDto);
		NRSHome nrsHome = PageFactory.initElements(driver, NRSHome.class);
		nrsHome.setDependencies(logger, testDataHelper);
		nrsHome.enterEntryCode(entryCode2);
		nrsHome.clickAddEntryCode();
		Thread.sleep(3000);
		nrsHome.clickShowName(automationShowName);
		nrsHome.clickViewPresentationDocumentOnly();
	}
}
