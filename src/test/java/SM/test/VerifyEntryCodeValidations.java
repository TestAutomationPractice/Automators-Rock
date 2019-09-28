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
import Pages.ShowManagement.Review;
import Pages.ShowManagement.SMHome;
import Pages.ShowManagement.ShowDetails;

public class VerifyEntryCodeValidations extends Base{

	
	private static String automationShowName = "AutoDocOnly" + ComponentBase.dateTime();
	private String entryCode = "EntryCode" + generateNumInStringFormat(22);

	
	public VerifyEntryCodeValidations() {
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
		
		ShowDetails showdetails = PageFactory.initElements(driver, ShowDetails.class);
		showdetails.setDependencies(logger, testDataHelper);
		showdetails.enterUnderwriter(testDataHelper.getValue("Underwriter"));
		showdetails.enterunderwriterRole(testDataHelper.getValue("UnderwriterRole"));
		showdetails.clickAdd();
		showdetails.selectMediaType(ShowType.Document_Only.getShowType());
		showdetails.enterShowName(automationShowName);
		showdetails.enterShowTitle(automationShowName);
		showdetails.selectBusinessGroup(testDataHelper.getValue("BusinessGroup"));
		showdetails.selectDealType(testDataHelper.getValue("DealType"));
		showdetails.selectSector(testDataHelper.getValue("Sector"));
		showdetails.enterStartDate(ComponentBase.modDate(-3));
		showdetails.enterEndDate(ComponentBase.modDate(6));
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
		entryCodes.verifyAccessControlLink(false);
		entryCodes.enterEntryCode(entryCode);
		Thread.sleep(3000);
		entryCodes.clickAdd();
		entryCodes.verifyAccessControlLink(false);
		entryCodes.VerifyEntryCodeLengthValidation();
		entryCodes.ClearEntryCodeInputBox();
		entryCode = entryCode.substring(0,15);
		entryCodes.enterEntryCode(entryCode);
		Thread.sleep(3000);
		entryCodes.clickAdd();
		Thread.sleep(2000);
		entryCodes.verifyAccessControlLink(true);
		entryCodes.clickEditEntryCode();
		entryCodes.enterEntryCode("ab");
		entryCodes.clickUpdate();
		entryCodes.VerifyEntryCodePresence(entryCode+"ab");
		
		// Enter 2nd entry code
		entryCodes.clickGenerateRandom();
		entryCodes.enterStartDate(ComponentBase.modDate(-1));
		entryCodes.enterEndDate(ComponentBase.modDate(4));
		entryCodes.selectTimezone(testDataHelper.getValue("UTCTimezone"));
		Thread.sleep(3000);
		entryCodes.clickAdd();
		
		// Enter 3rd entry code
		entryCodes.clickGenerateRandom();
		entryCodes.enterStartDate(ComponentBase.modDate(-1));
		entryCodes.enterEndDate(ComponentBase.modDate(4));
		entryCodes.selectTimezone(testDataHelper.getValue("ESTTimezone"));
		Thread.sleep(3000);
		entryCodes.clickAdd();
		
		
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


}
