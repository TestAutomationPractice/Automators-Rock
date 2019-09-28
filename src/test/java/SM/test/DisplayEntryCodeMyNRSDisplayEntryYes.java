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

public class DisplayEntryCodeMyNRSDisplayEntryYes extends Base{

	private String automationShowName = "AutoSMCall" + generateNumInStringFormat(4) + ComponentBase.dateTime();
	private String entryCode = "EC" + generateNumInStringFormat(5);
	String callPageURL;
	String conferencingURL;
	
	public DisplayEntryCodeMyNRSDisplayEntryYes() {
		testRunType = TestRunType.Regression;
		userName = Configuration.getInstance().getSiteAdminUserName();
		password = Configuration.getInstance().getSiteAdminPassword();
		userRole = UserRole.SiteAdmin;
	}
	
	@Test(description = "create Show With Review And Regular EntryCode", enabled = true, priority=0)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void createShowWithReviewAndRegularEntryCode() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		//Login and Create a new Conference Call on NRS

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
		showdetails.setDisplayEntryCodeReviewersToYes();
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
		documentaion.clickSaveAndContinue();

		Disclaimer disclaimer = PageFactory.initElements(driver, Disclaimer.class);
		disclaimer.setDependencies(logger, testDataHelper);
		disclaimer.clickSaveAndContinue();

		EntryCodes entryCodes = PageFactory.initElements(driver, EntryCodes.class);
		entryCodes.setDependencies(logger, testDataHelper);
		
		entryCodes.enterEntryCode(entryCode +"Regular");
		Thread.sleep(2000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		
		entryCodes.enterEntryCode(entryCode+"Review");
		entryCodes.selectEntryCodeType("Review");
		Thread.sleep(2000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		
		entryCodes.enterEntryCode("Regular"+ entryCode);
		Thread.sleep(2000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		
		entryCodes.clickContinue();
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
	
	@Test(description = "verify Entry Code Status On MyNrs", enabled = true, dependsOnMethods={"createShowWithReviewAndRegularEntryCode"},priority=1)
	@CustomTestAnnotations(testCaseNumber = "TC-119")
	public void verifyEntryCodeStatusOnMyNrs() throws Throwable, Exception {
		CommonTestDataDto testDataDto = commonTestData.get();
		RemoteWebDriver driver = testDataDto.getDriver();
		Log4JLogger logger = testDataDto.getLogger();
		TestDataHelper testDataHelper = testDataDto.getTestDataHelper();
		// Login and Create a new Conference Call on NRS
		NRSLogin nRSLogin = PageFactory.initElements(driver, NRSLogin.class);
		nRSLogin.setDependencies(logger, testDataHelper);
		nRSLogin.loginSM(testDataHelper.getValue("SMURL"), testDataHelper.getValue("RegularAccount"), testDataHelper.getValue("RegularAccountPassword"), testDataDto );

		NRSHome nRSHome = PageFactory.initElements(driver, NRSHome.class);
		nRSHome.setDependencies(logger, testDataHelper);
		nRSHome.enterEntryCode(entryCode+"Review");
		nRSHome.clickAddEntryCode();
		nRSHome.clickShowName(automationShowName);
		Thread.sleep(5000);
		nRSHome.verifyRegularEntryCodePresennceOnMYnrs(entryCode +"Regular");
		nRSHome.verifyRegularEntryCodePresennceOnMYnrs("Regular"+ entryCode);
		
		
	}
	
}
